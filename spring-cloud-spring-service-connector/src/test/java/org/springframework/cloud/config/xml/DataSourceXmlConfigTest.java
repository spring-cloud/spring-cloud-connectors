package org.springframework.cloud.config.xml;

import java.util.Properties;
import javax.sql.DataSource;

import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cloud.config.DataSourceCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.relational.HikariCpPooledDataSourceCreator;
import org.springframework.cloud.service.relational.TomcatJdbcPooledDataSourceCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import static org.springframework.cloud.config.DataSourceCloudConfigTestHelper.assertConnectionProperties;
import static org.springframework.cloud.config.DataSourceCloudConfigTestHelper.assertConnectionProperty;
import static org.springframework.cloud.config.DataSourceCloudConfigTestHelper.assertDataSourceType;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class DataSourceXmlConfigTest extends AbstractServiceXmlConfigTest<DataSource> {
	protected abstract String getDriverClassName();
	protected abstract String getValidationQuery();

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
	public void cloudDataSourceWithNoConfig() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));
		
		DataSource ds = testContext.getBean("no-config", getConnectorType());
		assertConnectionProperties(ds, null);
		assertConnectionProperty(ds, "driverClassName", getDriverClassName());
		assertConnectionProperty(ds, "validationQuery", getValidationQuery());
	}

	@Test
	public void cloudDataSourceWithMaxPool() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("pool-and-connection-config", getConnectorType());
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
		
		DataSource ds = testContext.getBean("pool-config", getConnectorType());
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 30, 5, 3000);
	}

	@Test
	public void cloudDataSourceWithConnectionProperties() {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("properties-config", getConnectorType());
		assertConnectionProperty(ds, "driverClassName", "test.driver");
		assertConnectionProperty(ds, "validationQuery", "test validation query");
		assertConnectionProperty(ds, "testOnBorrow", false);
	}

	@Test
	public void cloudDataSourceWithTomcatJdbcDataSource() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("db-pool-tomcat-jdbc", getConnectorType());
		assertDataSourceType(ds, TomcatJdbcPooledDataSourceCreator.TOMCAT_JDBC_DATASOURCE);
	}

	@Test
	public void cloudDataSourceWithHikariCpDataSource() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("db-pool-hikari", getConnectorType());
		assertDataSourceType(ds, HikariCpPooledDataSourceCreator.HIKARI_DATASOURCE);
	}

	@Test
	public void cloudDataSourceWithInvalidDataSource() throws Exception {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));

		DataSource ds = testContext.getBean("db-pool-invalid", getConnectorType());
		assertDataSourceType(ds, SimpleDriverDataSource.class);
	}
}
