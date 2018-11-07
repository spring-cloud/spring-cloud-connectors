package org.springframework.cloud.service.relational;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.cloud.ReflectionUtils;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.service.Util.hasClass;
import static org.springframework.cloud.service.relational.BasicDbcpPooledDataSourceCreator.DBCP2_BASIC_DATASOURCE;
import static org.springframework.cloud.service.relational.HikariCpPooledDataSourceCreator.HIKARI_DATASOURCE;
import static org.springframework.cloud.service.relational.TomcatDbcpPooledDataSourceCreator.TOMCAT_7_DBCP;
import static org.springframework.cloud.service.relational.TomcatDbcpPooledDataSourceCreator.TOMCAT_8_DBCP;
import static org.springframework.cloud.service.relational.TomcatJdbcPooledDataSourceCreator.TOMCAT_JDBC_DATASOURCE;

public class PooledDataSourceCreatorsTest {
	private static final int MIN_POOL_SIZE = 100;
	private static final int MAX_POOL_SIZE = 200;
	private static final int MAX_WAIT_TIME = 5;
	private static final int DEFAULT_MIN_POOL_SIZE = 0;
	private static final int DEFAULT_MAX_POOL_SIZE = 4;
	private static final int DEFAULT_MAX_WAIT_TIME = 30000;

	private static final String CONNECTION_PROPERTIES_STRING = "useUnicode=true;characterEncoding=UTF-8";
	private static final Properties CONNECTION_PROPERTIES = new Properties() {{
		setProperty("useUnicode", "true");
		setProperty("characterEncoding", "UTF-8");
	}};

	@Mock
	private MysqlServiceInfo mockMysqlServiceInfo;

	// Just to grab driver class name and validation query string 
	private MysqlDataSourceCreator mysqlDataSourceCreator = new MysqlDataSourceCreator();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(mockMysqlServiceInfo.getJdbcUrl()).thenReturn("jdbc:mysql://myuser:mypassword@10.20.30.40:3306/database-123");
	}

	@Test
	public void pooledDataSourceCreationDefault() throws Exception {
		PoolConfig poolConfig = new PoolConfig(MIN_POOL_SIZE, MAX_POOL_SIZE, MAX_WAIT_TIME);
		ConnectionConfig connectionConfig = new ConnectionConfig(CONNECTION_PROPERTIES_STRING);
		DataSourceConfig config = new DataSourceConfig(poolConfig, connectionConfig);
		DataSource ds = createMysqlDataSource(config);
		assertTomcatJdbcDataSource(ds, true);
	}

	@Test
	public void pooledDataSourceCreationDefaultPools() throws Exception {
		DataSource ds = createMysqlDataSource(null);
		assertTomcatJdbcDataSource(ds, false);
	}

	@Test
	public void pooledDataSourceCreationDbcp() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("BasicDbcp");
		assertBasicDbcpDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(BasicDbcpPooledDataSourceCreator.class.getSimpleName());
		assertBasicDbcpDataSource(ds);
	}

	@Test
	public void pooledDataSourceCreationTomcatDbcp() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("TomcatDbcp");
		assertTomcatDbcpDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(TomcatDbcpPooledDataSourceCreator.class.getSimpleName());
		assertTomcatDbcpDataSource(ds);
	}

	@Test
	public void pooledDataSourceCreationTomcatJdbc() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("TomcatJdbc");
		assertTomcatJdbcDataSource(ds, true);

		ds = createMysqlDataSourceWithPooledName(TomcatJdbcPooledDataSourceCreator.class.getSimpleName());
		assertTomcatJdbcDataSource(ds, true);
	}

	@Test
	public void pooledDataSourceCreationHikariCP() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("HikariCp");
		assertHikariDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(HikariCpPooledDataSourceCreator.class.getSimpleName());
		assertHikariDataSource(ds);
	}

	@Test
	public void pooledDataSourceCreationInvalid() {
		DataSource ds = createMysqlDataSourceWithPooledName("Dummy");
		assertThat(ds, instanceOf(UrlDecodingDataSource.class));

		ds = ((UrlDecodingDataSource) ds).getTargetDataSource();

		assertThat(ds, instanceOf(org.springframework.jdbc.datasource.SimpleDriverDataSource.class));
	}

	private DataSource createMysqlDataSourceWithPooledName(String pooledDataSourceName) {
		List<String> dataSourceNames = Collections.singletonList(pooledDataSourceName);
		PoolConfig poolConfig =
				new PoolConfig(MIN_POOL_SIZE, MAX_POOL_SIZE, MAX_WAIT_TIME);
		ConnectionConfig connectionConfig = new ConnectionConfig(CONNECTION_PROPERTIES_STRING);
		DataSourceConfig config = new DataSourceConfig(poolConfig, connectionConfig, dataSourceNames);
		return createMysqlDataSource(config);
	}

	private DataSource createMysqlDataSource(ServiceConnectorConfig config) {
		return mysqlDataSourceCreator.create(mockMysqlServiceInfo, config);
	}

	private void assertBasicDbcpDataSource(DataSource ds) throws ClassNotFoundException {
		assertThat(ds, instanceOf(UrlDecodingDataSource.class));

		ds = ((UrlDecodingDataSource) ds).getTargetDataSource();

		assertThat(ds, instanceOf(Class.forName(DBCP2_BASIC_DATASOURCE)));

		assertEquals(MIN_POOL_SIZE, getIntValue(ds, "minIdle"));
		assertEquals(MAX_POOL_SIZE, getIntValue(ds, "maxTotal"));
		assertEquals(MAX_WAIT_TIME, getIntValue(ds, "maxWaitMillis"));
		assertEquals(CONNECTION_PROPERTIES, getPropertiesValue(ds, "connectionProperties"));
	}

	private void assertTomcatDbcpDataSource(DataSource ds) throws ClassNotFoundException {
		assertThat(ds, instanceOf(UrlDecodingDataSource.class));

		ds = ((UrlDecodingDataSource) ds).getTargetDataSource();

		assertTrue(hasClass(TOMCAT_7_DBCP) || hasClass(TOMCAT_8_DBCP));

		if (hasClass(TOMCAT_7_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_7_DBCP)));

			assertEquals(MIN_POOL_SIZE, getIntValue(ds, "minIdle"));
			assertEquals(MAX_WAIT_TIME, getIntValue(ds, "maxWait"));
			assertEquals(CONNECTION_PROPERTIES, getPropertiesValue(ds, "connectionProperties"));
		}
		if (hasClass(TOMCAT_8_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_8_DBCP)));

			assertEquals(MIN_POOL_SIZE, getIntValue(ds, "minIdle"));
			assertEquals(MAX_POOL_SIZE, getIntValue(ds, "maxTotal"));
			assertEquals(MAX_WAIT_TIME, getIntValue(ds, "maxWaitMillis"));
			assertEquals(CONNECTION_PROPERTIES, getPropertiesValue(ds, "connectionProperties"));
		}
	}

	private void assertTomcatJdbcDataSource(DataSource ds, boolean overrideConfig) throws ClassNotFoundException {
		assertThat(ds, instanceOf(UrlDecodingDataSource.class));

		ds = ((UrlDecodingDataSource) ds).getTargetDataSource();

		assertThat(ds, instanceOf(Class.forName(TOMCAT_JDBC_DATASOURCE)));

		if (overrideConfig) {
			assertEquals(MIN_POOL_SIZE, getIntValue(ds, "minIdle"));
			assertEquals(MAX_POOL_SIZE, getIntValue(ds, "maxActive"));
			assertEquals(MAX_WAIT_TIME, getIntValue(ds, "maxWait"));
			// the results of setConnectionProperties are reflected by getDbProperties, not getConnectionProperties
			assertEquals(CONNECTION_PROPERTIES, getPropertiesValue(ds, "dbProperties"));
		} else {
			assertEquals(DEFAULT_MIN_POOL_SIZE, getIntValue(ds, "minIdle"));
			assertEquals(DEFAULT_MAX_POOL_SIZE, getIntValue(ds, "maxActive"));
			assertEquals(DEFAULT_MAX_WAIT_TIME, getIntValue(ds, "maxWait"));
			assertEquals(Collections.emptyMap(), getPropertiesValue(ds, "dbProperties"));
		}
	}

	private void assertHikariDataSource(DataSource ds) throws ClassNotFoundException {
		assertThat(ds, instanceOf(UrlDecodingDataSource.class));

		ds = ((UrlDecodingDataSource) ds).getTargetDataSource();

		assertThat(ds, instanceOf(Class.forName(HIKARI_DATASOURCE)));

		assertEquals(MIN_POOL_SIZE, getIntValue(ds, "minimumIdle"));
		assertEquals(MAX_POOL_SIZE, getIntValue(ds, "maximumPoolSize"));
	}

	private int getIntValue(Object target, String fieldName) {
		return Integer.valueOf(getStringValue(target, fieldName));
	}

	private String getStringValue(Object target, String fieldName) {
		return getValue(target, fieldName).toString();
	}

	private Properties getPropertiesValue(Object target, String fieldName) {
		return (Properties) getValue(target, fieldName);
	}

	private Object getValue(Object target, String fieldName) {
		Object value = ReflectionUtils.getValue(target, fieldName);
		if (value == null) {
			throw new IllegalArgumentException("Bad field name " + fieldName + " for target object " + target);
		}
		return value;
	}
}
