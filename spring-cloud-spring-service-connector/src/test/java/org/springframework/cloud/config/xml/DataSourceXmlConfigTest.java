package org.springframework.cloud.config.xml;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cloud.config.DataSourceCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.relational.BasicDbcpPooledDataSourceCreator;
import org.springframework.cloud.service.relational.HikariCpPooledDataSourceCreator;
import org.springframework.cloud.service.relational.TomcatJdbcPooledDataSourceCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class DataSourceXmlConfigTest extends AbstractServiceXmlConfigTest<DataSource> {

	protected abstract ServiceInfo createService(String id);
	
	protected String getWithServiceIdContextFileName() {
		return "cloud-datasource-with-service-id.xml";
	}
	
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-datasource-without-service-id.xml";
	}

	protected Class<DataSource> getConnectorType() {
		return DataSource.class;
	}

	// Mixed relational services test (mysql+postgresql)
	@Test(expected=BeanCreationException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byType() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected=BeanCreationException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byId() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean(getConnectorType());
	}
	
	@Test
	public void cloudDataSourceWithMaxPool() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));
		
		DataSource ds = testContext.getBean("db-pool20-wait200", getConnectorType());
		assertThat(ds, instanceOf(Class.forName(BasicDbcpPooledDataSourceCreator.DBCP2_BASIC_DATASOURCE)));
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 20, 0, 200);
		
		Properties connectionProp = new Properties();
		connectionProp.put("sessionVariables", "sql_mode='ANSI'");
		connectionProp.put("characterEncoding", "UTF-8");
		DataSourceCloudConfigTestHelper.assertConnectionProperties(ds, connectionProp);
	}
	
	@Test
	public void cloudDataSourceWithMinMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));
		
		DataSource ds = testContext.getBean("db-pool5-30-wait3000", getConnectorType());
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 30, 5, 3000);
	}

	@Test
	public void cloudDataSourceWithTomcatJdbcDataSource() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("db-pool-tomcat-jdbc", getConnectorType());
		assertThat(ds, instanceOf(Class.forName(TomcatJdbcPooledDataSourceCreator.TOMCAT_JDBC_DATASOURCE)));
	}

	@Test
	public void cloudDataSourceWithHikariCpDataSource() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("db-pool-hikari", getConnectorType());
		assertThat(ds, instanceOf(Class.forName(HikariCpPooledDataSourceCreator.HIKARI_DATASOURCE)));
	}

	@Test
	public void cloudDataSourceWithInvalidDataSource() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("db-pool-invalid", getConnectorType());
		assertThat(ds, instanceOf(SimpleDriverDataSource.class));
	}
}
