package org.springframework.cloud.service.relational;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.cloud.config.DataSourceCloudConfigTestHelper;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractDataSourceCreatorTest<C extends DataSourceCreator<SI>, SI extends RelationalServiceInfo> {
	public abstract  SI createServiceInfo();
	public abstract String getDriverName();
	public abstract C getCreator();
	public abstract String getValidationQueryStart();
	
	@Test
	public void cloudDataSourceCreationNoConfig() throws Exception {
		SI relationalServiceInfo = createServiceInfo();

		DataSource dataSource = getCreator().create(relationalServiceInfo, null);

		assertDataSourceProperties(relationalServiceInfo, dataSource);
	}

	@Test
	public void cloudDataSourceCreationWithConfig() throws Exception {
		SI relationalServiceInfo = createServiceInfo();

		DataSourceConfig config = new DataSourceConfig(new PoolConfig("5", 100), new ConnectionConfig("foo=bar"));
		DataSource dataSource = getCreator().create(relationalServiceInfo, config);
		
		assertDataSourceProperties(relationalServiceInfo, dataSource);
		DataSourceCloudConfigTestHelper.assertPoolProperties(dataSource, 5, 0, 100);
		
		Properties connectionProp = new Properties();
		connectionProp.put("foo", "bar");
		assertConnectionProperties(dataSource, connectionProp);
	}
	
	private void assertConnectionProperties(DataSource dataSource, Properties connectionProp) {
		assertEquals(connectionProp, ReflectionTestUtils.getField(dataSource, "connectionProperties"));
	}

	private void assertDataSourceProperties(RelationalServiceInfo relationalServiceInfo, DataSource dataSource) {
		assertNotNull(dataSource);

		assertEquals(getDriverName(), ReflectionTestUtils.getField(dataSource, "driverClassName"));
		assertEquals(relationalServiceInfo.getJdbcUrl(), ReflectionTestUtils.getField(dataSource, "url"));
		assertTrue((Boolean) ReflectionTestUtils.invokeGetterMethod(dataSource, "testOnBorrow"));
		assertNotNull(ReflectionTestUtils.invokeGetterMethod(dataSource, "validationQuery"));
		assertTrue(((String) ReflectionTestUtils.invokeGetterMethod(dataSource, "validationQuery")).startsWith(getValidationQueryStart()));
	}
	
}
