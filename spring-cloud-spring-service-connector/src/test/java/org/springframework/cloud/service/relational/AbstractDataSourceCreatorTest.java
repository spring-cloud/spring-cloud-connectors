package org.springframework.cloud.service.relational;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import org.junit.Test;

import org.springframework.cloud.ReflectionUtils;
import org.springframework.cloud.config.DataSourceCloudConfigTestHelper;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

		List<String> pooledDataSource = Collections.singletonList(BasicDbcpPooledDataSourceCreator.class.getSimpleName());
		DataSourceConfig config = new DataSourceConfig(pooledDataSource);
		DataSource dataSource = getCreator().create(relationalServiceInfo, config);

		assertDataSourceProperties(relationalServiceInfo, dataSource);
	}

	@Test
	public void cloudDataSourceCreationWithConfig() throws Exception {
		SI relationalServiceInfo = createServiceInfo();

		PoolConfig poolConfig = new PoolConfig("5", 100);
		ConnectionConfig connectionConfig = new ConnectionConfig("foo=bar");
		List<String> pooledDataSource = Collections.singletonList(BasicDbcpPooledDataSourceCreator.class.getSimpleName());
		DataSourceConfig config = new DataSourceConfig(poolConfig, connectionConfig, pooledDataSource);
		DataSource dataSource = getCreator().create(relationalServiceInfo, config);
		
		assertDataSourceProperties(relationalServiceInfo, dataSource);
		DataSourceCloudConfigTestHelper.assertPoolProperties(dataSource, 5, 0, 100);
		
		Properties connectionProp = new Properties();
		connectionProp.put("foo", "bar");
		assertConnectionProperties(dataSource, connectionProp);
	}
	
	private void assertConnectionProperties(DataSource dataSource, Properties connectionProp) {
		if (dataSource instanceof DelegatingDataSource) {
			dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
		}
		assertEquals(connectionProp, ReflectionTestUtils.getField(dataSource, "connectionProperties"));
	}

	private void assertDataSourceProperties(RelationalServiceInfo relationalServiceInfo, DataSource dataSource) {
		assertNotNull(dataSource);

		if (dataSource instanceof DelegatingDataSource) {
			dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
		}

		assertNotNull(dataSource);

		assertEquals(getDriverName(), ReflectionUtils.getValue(dataSource, "driverClassName"));
		assertEquals(relationalServiceInfo.getJdbcUrl(), ReflectionUtils.getValue(dataSource, "url"));

		Object testOnBorrow = ReflectionUtils.getValue(dataSource, "testOnBorrow");
		assertNotNull(testOnBorrow);
		assertTrue(Boolean.valueOf(testOnBorrow.toString()));

		Object validationQuery = ReflectionUtils.getValue(dataSource, "validationQuery");
		assertNotNull(validationQuery);
		assertTrue(validationQuery.toString().startsWith(getValidationQueryStart()));
	}
	
}
