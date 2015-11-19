package org.springframework.cloud.config.java;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cloud.config.DataSourceCloudConfigTestHelper;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.relational.BasicDbcpPooledDataSourceCreator;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Common base class for testing datasource-related Java config
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class DataSourceJavaConfigTest extends AbstractServiceJavaConfigTest<DataSource> {
	
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
	public void cloudDataSourceWithMaxPool() {
		ApplicationContext testContext = getTestApplicationContext(DatasourceConfigWithServiceConfig.class,
				createService("my-service"));
		
		DataSource ds = testContext.getBean("dbPool20Wait200", getConnectorType());
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 20, 0, 200);
		
		Properties connectionProp = new Properties();
		connectionProp.put("sessionVariables", "sql_mode='ANSI'");
		connectionProp.put("characterEncoding", "UTF-8");
		DataSourceCloudConfigTestHelper.assertConnectionProperties(ds, connectionProp);
	}
	
	@Test
	public void cloudDataSourceWithMinMaxPool() {
		ApplicationContext testContext = getTestApplicationContext(DatasourceConfigWithServiceConfig.class,
				createService("my-service"));
		
		DataSource ds = testContext.getBean("dbPool5_20Wait3000", getConnectorType());
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 30, 5, 3000);
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
	public DataSource dbPool20Wait200() { // use this name so that we have a case with default name
		PoolConfig poolConfig = new PoolConfig(20, 200);
		ConnectionConfig connectionConfig = new ConnectionConfig("sessionVariables=sql_mode='ANSI';characterEncoding=UTF-8");
		DataSourceConfig serviceConfig = new DataSourceConfig(poolConfig, connectionConfig, basicDbcpConnectionPool());
		return connectionFactory().dataSource("my-service", serviceConfig);
	}

	@Bean
	public DataSource dbPool5_20Wait3000() { // use this name so that we have a case with default name
		PoolConfig poolConfig = new PoolConfig(5, 30, 3000);
		DataSourceConfig serviceConfig = new DataSourceConfig(poolConfig, null, basicDbcpConnectionPool());
		return connectionFactory().dataSource("my-service", serviceConfig);
	}

	private List<String> basicDbcpConnectionPool() {
		return Collections.singletonList(BasicDbcpPooledDataSourceCreator.class.getSimpleName());
	}
}