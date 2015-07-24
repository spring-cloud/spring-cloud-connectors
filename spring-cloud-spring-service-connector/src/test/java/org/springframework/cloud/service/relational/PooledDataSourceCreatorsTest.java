package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.MysqlServiceInfo;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.cloud.service.Util.hasClass;
import static org.springframework.cloud.service.relational.TomcatDbcpPooledDataSourceCreator.TOMCAT_7_DBCP;
import static org.springframework.cloud.service.relational.TomcatDbcpPooledDataSourceCreator.TOMCAT_8_DBCP;

public class PooledDataSourceCreatorsTest {
	@Mock private MysqlServiceInfo mockMysqlServiceInfo;

	// Just to grab driver class name and validation query string 
	private MysqlDataSourceCreator mysqlDataSourceCreator = new MysqlDataSourceCreator();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void pooledDataSourceCreationDbcp() {
		assertPooledDataSource(new BasicDbcpPooledDataSourceCreator<MysqlServiceInfo>());
	}

	@Test
	public void pooledDataSourceCreationTomcatDbcp() throws Exception {
		DataSource ds = assertPooledDataSource(new TomcatDbcpPooledDataSourceCreator<MysqlServiceInfo>());
		assertTrue(hasClass(TOMCAT_7_DBCP) || hasClass(TOMCAT_8_DBCP));

		if (hasClass(TOMCAT_7_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_7_DBCP)));
		}
		if (hasClass(TOMCAT_8_DBCP)) {
			assertThat(ds, instanceOf(Class.forName(TOMCAT_8_DBCP)));
		}
	}

	@Test
	public void pooledDataSourceCreationTomcatHighPerformance() {
		assertPooledDataSource(new TomcatHighPerformancePooledDataSourceCreator<MysqlServiceInfo>());
	}

	@Test
	public void pooledDataSourceCreationHikariCP() {
		assertPooledDataSource(new HikariCpPooledDataSourceCreator<MysqlServiceInfo>());
	}

	private DataSource assertPooledDataSource(PooledDataSourceCreator<MysqlServiceInfo> testCreator) {
		DataSource ds = testCreator.create(mockMysqlServiceInfo, null, 
				mysqlDataSourceCreator.getDriverClassName(mockMysqlServiceInfo), 
				"select 1");

		Assert.assertNotNull("Failed to create datasource with " + testCreator.getClass().getSimpleName(), ds);

		return ds;
	}
}
