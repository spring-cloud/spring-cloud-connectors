package org.springframework.cloud.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cloud.Cloud;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericCloudServiceConnectorFactoryTest {
	@Mock Cloud mockCloud;
	@Mock DataSource mockDataSource;

	private final String serviceId = "mysql-db";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void creatingBoundServiceWithoutConnectorType() throws Exception {
		when(mockCloud.getServiceConnector(serviceId, null, null)).thenReturn(mockDataSource);
		
		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(serviceId, null);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();
		
		assertSame(mockDataSource, testFactory.getObject());
	}

	@Test
	public void creatingBoundServiceWithConnectorType() throws Exception {
		when(mockCloud.getServiceConnector(serviceId, DataSource.class, null)).thenReturn(mockDataSource);
		
		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(serviceId, null);
		testFactory.setServiceConnectorType(DataSource.class);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();
		
		assertSame(mockDataSource, testFactory.getObject());
	}

	@Test
	public void creatingBoundServiceWithFactoryConnectorType() throws Exception {
		when(mockCloud.getServiceConnector(serviceId, DataSourceFactory.class, null)).thenReturn(new DataSourceFactory());

		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(serviceId, null);
		testFactory.setServiceConnectorType(DataSourceFactory.class);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();

		assertSame(mockDataSource, testFactory.getObject());
	}

	@Test
	public void creatingBoundServiceWithIncorrectConnectorType() throws Exception {
		when(mockCloud.getServiceConnector(serviceId, DataSource.class, null)).thenReturn(mockDataSource);
		
		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(serviceId, null);
		testFactory.setServiceConnectorType(String.class);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();
		
		assertNull(testFactory.getObject());
	}

	class DataSourceFactory implements FactoryBean<DataSource> {
		@Override
		public DataSource getObject() throws Exception {
			return mockDataSource;
		}

		@Override
		public Class<?> getObjectType() {
			return mockDataSource.getClass();
		}

		@Override
		public boolean isSingleton() {
			return true;
		}
	}
}
