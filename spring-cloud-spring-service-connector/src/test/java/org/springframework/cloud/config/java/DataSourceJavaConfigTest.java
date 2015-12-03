package org.springframework.cloud.config.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.relational.BasicDbcpPooledDataSourceCreator;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.springframework.cloud.config.DataSourceCloudConfigTestHelper.assertConnectionProperties;
import static org.springframework.cloud.config.DataSourceCloudConfigTestHelper.assertConnectionProperty;
import static org.springframework.cloud.config.DataSourceCloudConfigTestHelper.assertPoolProperties;

/**
 * Common base class for testing datasource-related Java config
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class DataSourceJavaConfigTest extends AbstractServiceJavaConfigTest<DataSource> {
	protected abstract String getDriverClassName();
	protected abstract String getValidationQuery();

	public DataSourceJavaConfigTest() {
		super(DatasourceConfigWithId.class, DatasourceConfigWithoutId.class);
	}
	
	protected Class<DataSource> getConnectorType() {
		return DataSource.class;
	}
	
	// Mixed relational services test (mysql+postgresql)
	@Test(expected=BeanCreationException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byType() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId(
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected=BeanCreationException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byId() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId(
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean(getConnectorType());
	}
	
	@Test
	public void cloudDataSourceWithNoConfig() {
		ApplicationContext testContext = getTestApplicationContext(DatasourceConfigWithServiceConfig.class,
				createService("my-service"));
		
		DataSource ds = testContext.getBean("dataSourceWithNoConfig", getConnectorType());

		assertConnectionProperties(ds, null);
		assertConnectionProperty(ds, "driverClassName", getDriverClassName());
		assertConnectionProperty(ds, "validationQuery", getValidationQuery());
	}

	@Test
	public void cloudDataSourceWithMaxPool() {
		ApplicationContext testContext = getTestApplicationContext(DatasourceConfigWithServiceConfig.class,
				createService("my-service"));

		DataSource ds = testContext.getBean("dataSourceWithPoolAndConnectionConfig", getConnectorType());
		assertPoolProperties(ds, 20, 0, 200);

		Properties connectionProp = new Properties();
		connectionProp.put("sessionVariables", "sql_mode='ANSI'");
		connectionProp.put("characterEncoding", "UTF-8");
		assertConnectionProperties(ds, connectionProp);
	}

	@Test
	public void cloudDataSourceWithMinMaxPool() {
		ApplicationContext testContext = getTestApplicationContext(DatasourceConfigWithServiceConfig.class,
				createService("my-service"));
		
		DataSource ds = testContext.getBean("dataSourceWithPoolConfig", getConnectorType());
		assertPoolProperties(ds, 30, 5, 3000);
	}

	@Test
	public void cloudDataSourceWithConnectionProperties() {
		ApplicationContext testContext = getTestApplicationContext(DatasourceConfigWithServiceConfig.class,
				createService("my-service"));

		DataSource ds = testContext.getBean("dataSourceWithConnectionPropertiesConfig", getConnectorType());
		assertConnectionProperty(ds, "driverClassName", "test.driver");
		assertConnectionProperty(ds, "validationQuery", "test validation query");
		assertConnectionProperty(ds, "testOnBorrow", false);
	}
}

class DatasourceConfigWithId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public DataSource testDatasource() {
		return connectionFactory().dataSource("my-service");
	}
}

class DatasourceConfigWithoutId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public DataSource testDatasource() {
		return connectionFactory().dataSource();
	}
}

class DatasourceConfigWithServiceConfig extends AbstractCloudConfig {
	@Bean
	public DataSource dataSourceWithNoConfig() {
		return connectionFactory().dataSource("my-service");
	}

	@Bean
	public DataSource dataSourceWithPoolAndConnectionConfig() {
		PoolConfig poolConfig = new PoolConfig(20, 200);
		ConnectionConfig connectionConfig = new ConnectionConfig("sessionVariables=sql_mode='ANSI';characterEncoding=UTF-8");
		DataSourceConfig serviceConfig = new DataSourceConfig(poolConfig, connectionConfig, basicDbcpConnectionPool());
		return connectionFactory().dataSource("my-service", serviceConfig);
	}

	@Bean
	public DataSource dataSourceWithPoolConfig() {
		PoolConfig poolConfig = new PoolConfig(5, 30, 3000);
		DataSourceConfig serviceConfig = new DataSourceConfig(poolConfig, null, basicDbcpConnectionPool());
		return connectionFactory().dataSource("my-service", serviceConfig);
	}

	@Bean
	public DataSource dataSourceWithConnectionPropertiesConfig() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("driverClassName", "test.driver");
		properties.put("validationQuery", "test validation query");
		properties.put("testOnBorrow", false);
		DataSourceConfig serviceConfig = new DataSourceConfig(null, null, basicDbcpConnectionPool(), properties);
		return connectionFactory().dataSource("my-service", serviceConfig);
	}

	private List<String> basicDbcpConnectionPool() {
		return Collections.singletonList(BasicDbcpPooledDataSourceCreator.class.getSimpleName());
	}
}