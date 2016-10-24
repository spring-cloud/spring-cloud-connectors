package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.ReflectionUtils;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.MysqlServiceInfo;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.service.Util.hasClass;
import static org.springframework.cloud.service.relational.BasicDbcpPooledDataSourceCreator.DBCP2_BASIC_DATASOURCE;
import static org.springframework.cloud.service.relational.BasicDbcpPooledDataSourceCreator.DBCP_BASIC_DATASOURCE;
import static org.springframework.cloud.service.relational.HikariCpPooledDataSourceCreator.HIKARI_DATASOURCE;
import static org.springframework.cloud.service.relational.TomcatDbcpPooledDataSourceCreator.TOMCAT_7_DBCP;
import static org.springframework.cloud.service.relational.TomcatDbcpPooledDataSourceCreator.TOMCAT_8_DBCP;
import static org.springframework.cloud.service.relational.TomcatJdbcPooledDataSourceCreator.TOMCAT_JDBC_DATASOURCE;

public class PooledDataSourceCreatorsTest {
	public static final int MIN_POOL_SIZE = 100;
	public static final int MAX_POOL_SIZE = 200;
	public static final int MAX_WAIT_TIME = 5;

	@Mock private MysqlServiceInfo mockMysqlServiceInfo;

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
		DataSourceConfig config = new DataSourceConfig(poolConfig, null);
		DataSource ds = createMysqlDataSource(config);
		assertTomcatJdbcDataSource(ds);
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
		assertTomcatJdbcDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(TomcatJdbcPooledDataSourceCreator.class.getSimpleName());
		assertTomcatJdbcDataSource(ds);
	}

	@Test
	public void pooledDataSourceCreationHikariCP() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("HikariCp");
		assertHikariDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(HikariCpPooledDataSourceCreator.class.getSimpleName());
		assertHikariDataSource(ds);
	}

	@Test
	public void pooledDataSourceCreationInvalid() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("Dummy");
		assertThat(ds, instanceOf(org.springframework.jdbc.datasource.SimpleDriverDataSource.class));
	}

	private DataSource createMysqlDataSourceWithPooledName(String pooledDataSourceName) {
		List<String> dataSourceNames = Collections.singletonList(pooledDataSourceName);
		PoolConfig poolConfig =
				new PoolConfig(MIN_POOL_SIZE, MAX_POOL_SIZE, MAX_WAIT_TIME);
		DataSourceConfig config = new DataSourceConfig(poolConfig, null, dataSourceNames);
		return createMysqlDataSource(config);
	}

	private DataSource createMysqlDataSource(ServiceConnectorConfig config) {
		return mysqlDataSourceCreator.create(mockMysqlServiceInfo, config);
	}

	private void assertBasicDbcpDataSource(DataSource ds) throws ClassNotFoundException {
		assertTrue(hasClass(DBCP2_BASIC_DATASOURCE) || hasClass(DBCP_BASIC_DATASOURCE));

		if (hasClass(DBCP2_BASIC_DATASOURCE)) {
			assertThat(ds, instanceOf(Class.forName(DBCP2_BASIC_DATASOURCE)));

			assertEquals(MIN_POOL_SIZE, getValue(ds, "minIdle"));
			assertEquals(MAX_POOL_SIZE, getValue(ds, "maxTotal"));
			assertEquals(MAX_WAIT_TIME, getValue(ds, "maxWaitMillis"));
		}
		if (hasClass(DBCP_BASIC_DATASOURCE) && !hasClass(DBCP2_BASIC_DATASOURCE)) {
			assertThat(ds, instanceOf(Class.forName(DBCP_BASIC_DATASOURCE)));

			assertEquals(MIN_POOL_SIZE, getValue(ds, "minIdle"));
			assertEquals(MAX_POOL_SIZE, getValue(ds, "maxActive"));
			assertEquals(MAX_WAIT_TIME, getValue(ds, "maxWait"));
		}
	}

	private void assertTomcatDbcpDataSource(DataSource ds) throws ClassNotFoundException {
		assertTrue(hasClass(TOMCAT_7_DBCP) || hasClass(TOMCAT_8_DBCP));

		if (hasClass(TOMCAT_7_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_7_DBCP)));

			assertEquals(MIN_POOL_SIZE, getValue(ds, "minIdle"));
			assertEquals(MAX_WAIT_TIME, getValue(ds, "maxWait"));
		}
		if (hasClass(TOMCAT_8_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_8_DBCP)));

			assertEquals(MIN_POOL_SIZE, getValue(ds, "minIdle"));
			assertEquals(MAX_POOL_SIZE, getValue(ds, "maxTotal"));
			assertEquals(MAX_WAIT_TIME, getValue(ds, "maxWaitMillis"));
		}
	}

	private void assertTomcatJdbcDataSource(DataSource ds) throws ClassNotFoundException {
		assertThat(ds, instanceOf(Class.forName(TOMCAT_JDBC_DATASOURCE)));

		assertEquals(MIN_POOL_SIZE, getValue(ds, "minIdle"));
		assertEquals(MAX_WAIT_TIME, getValue(ds, "maxWait"));
	}

	private void assertHikariDataSource(DataSource ds) throws ClassNotFoundException {
		assertThat(ds, instanceOf(Class.forName(HIKARI_DATASOURCE)));

		assertEquals(MIN_POOL_SIZE, getValue(ds, "minimumIdle"));
		assertEquals(MAX_POOL_SIZE, getValue(ds, "maximumPoolSize"));
	}

	private int getValue(Object target, String fieldName) {
		Object value = ReflectionUtils.getValue(target, fieldName);
		if (value == null) {
			throw new IllegalArgumentException("Bad field name " + fieldName + " for target object " + target);
		}
		return Integer.valueOf(value.toString());
	}
}
