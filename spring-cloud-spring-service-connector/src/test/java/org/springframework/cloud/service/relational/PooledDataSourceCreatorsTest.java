package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.MysqlServiceInfo;

import java.util.Collections;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
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
		DataSource ds = createMysqlDataSource(null);
		assertBasicDbcpDataSource(ds);
	}

	@Test
	public void pooledDataSourceCreationDbcp() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("BasicDbcp");
		assertBasicDbcpDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(BasicDbcpPooledDataSourceCreator.class.getSimpleName());
		assertBasicDbcpDataSource(ds);
	}

	private void assertBasicDbcpDataSource(DataSource ds) throws ClassNotFoundException {
		assertTrue(hasClass(DBCP2_BASIC_DATASOURCE) || hasClass(DBCP_BASIC_DATASOURCE));

		if (hasClass(DBCP2_BASIC_DATASOURCE)) {
			assertThat(ds, instanceOf(Class.forName(DBCP2_BASIC_DATASOURCE)));
		}
		if (hasClass(DBCP_BASIC_DATASOURCE) && !hasClass(DBCP2_BASIC_DATASOURCE)) {
			assertThat(ds, instanceOf(Class.forName(DBCP_BASIC_DATASOURCE)));
		}
	}

	@Test
	public void pooledDataSourceCreationTomcatDbcp() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("TomcatDbcp");
		assertTomcatDbcpDataSource(ds);

		ds = createMysqlDataSourceWithPooledName(TomcatDbcpPooledDataSourceCreator.class.getSimpleName());
		assertTomcatDbcpDataSource(ds);
	}

	private void assertTomcatDbcpDataSource(DataSource ds) throws ClassNotFoundException {
		assertTrue(hasClass(TOMCAT_7_DBCP) || hasClass(TOMCAT_8_DBCP));

		if (hasClass(TOMCAT_7_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_7_DBCP)));
		}
		if (hasClass(TOMCAT_8_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_8_DBCP)));
		}
	}

	@Test
	public void pooledDataSourceCreationTomcatJdbc() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("TomcatJdbc");
		assertThat(ds, instanceOf(Class.forName(TOMCAT_JDBC_DATASOURCE)));

		ds = createMysqlDataSourceWithPooledName(TomcatJdbcPooledDataSourceCreator.class.getSimpleName());
		assertThat(ds, instanceOf(Class.forName(TOMCAT_JDBC_DATASOURCE)));
	}

	@Test
	public void pooledDataSourceCreationHikariCP() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("HikariCp");
		assertThat(ds, instanceOf(Class.forName(HIKARI_DATASOURCE)));

		ds = createMysqlDataSourceWithPooledName(HikariCpPooledDataSourceCreator.class.getSimpleName());
		assertThat(ds, instanceOf(Class.forName(HIKARI_DATASOURCE)));
	}

	@Test
	public void pooledDataSourceCreationInvalid() throws Exception {
		DataSource ds = createMysqlDataSourceWithPooledName("Dummy");
		assertThat(ds, instanceOf(org.springframework.jdbc.datasource.SimpleDriverDataSource.class));
	}

	private DataSource createMysqlDataSourceWithPooledName(String pooledDataSourceName) {
		DataSourceConfig config = new DataSourceConfig(Collections.singletonList(pooledDataSourceName));
		return createMysqlDataSource(config);
	}

	private DataSource createMysqlDataSource(ServiceConnectorConfig config) {
		return mysqlDataSourceCreator.create(mockMysqlServiceInfo, config);
	}
}
