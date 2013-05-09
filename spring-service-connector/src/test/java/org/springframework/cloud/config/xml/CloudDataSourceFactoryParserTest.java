package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class CloudDataSourceFactoryParserTest extends AbstractCloudServiceConnectorFactoryParserTest<DataSource> {

	protected abstract BaseServiceInfo createService(String id);
	
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
	@Test(expected=CloudException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byType() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected=CloudException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byId() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean(getConnectorType());
	}
	
	@Test
	public void cloudDataSourceWithMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createMysqlService("my-service"));
		
		DataSource ds = testContext.getBean("db-pool20-wait200", getConnectorType());
		assertPoolProperties(ds, 20, 0, 200);
		
		Properties connectionProp = new Properties();
		connectionProp.put("sessionVariables", "sql_mode='ANSI'");
		connectionProp.put("characterEncoding", "UTF-8");
		assertConnectionProperties(ds, connectionProp);
	}
	
	@Test
	public void cloudDataSourceWithMinMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createMysqlService("my-service"));
		
		DataSource ds = testContext.getBean("db-pool5-30-wait3000", getConnectorType());
		assertPoolProperties(ds, 30, 5, 3000);
	}

	private void assertPoolProperties(DataSource dataSource, int maxActive, int minIdle, long maxWait) {
		assertEquals(maxActive, ReflectionTestUtils.getField(dataSource, "maxActive"));
		assertEquals(minIdle, ReflectionTestUtils.getField(dataSource, "minIdle"));
		assertEquals(maxWait, ReflectionTestUtils.getField(dataSource, "maxWait"));		
	}
	
	private void assertConnectionProperties(DataSource dataSource, Properties connectionProp) {
		assertEquals(connectionProp, ReflectionTestUtils.getField(dataSource, "connectionProperties"));
	}
	
}
