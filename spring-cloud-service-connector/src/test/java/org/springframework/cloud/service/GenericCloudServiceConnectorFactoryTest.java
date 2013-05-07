package org.springframework.cloud.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.Cloud;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericCloudServiceConnectorFactoryTest {
	@Mock Cloud mockCloud;
	@Mock DataSource mockDataSource;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void creatingBoundServiceWithoutConnectorType() throws Exception {
		String id = "mysql-db";
		
		when(mockCloud.getServiceConnector(id, null, null)).thenReturn(mockDataSource);
		
		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(id, null);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();
		
		assertSame(mockDataSource, testFactory.getObject());
	}

	@Test
	public void creatingBoundServiceWithConnectorType() throws Exception {
		String id = "mysql-db";
		
		when(mockCloud.getServiceConnector(id, DataSource.class, null)).thenReturn(mockDataSource);
		
		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(id, null);
		testFactory.setServiceConnectorType(DataSource.class);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();
		
		assertSame(mockDataSource, testFactory.getObject());
	}

	@Test
	public void creatingBoundServiceWithIncorrectConnectorType() throws Exception {
		String id = "mysql-db";
		
		when(mockCloud.getServiceConnector(id, DataSource.class, null)).thenReturn(mockDataSource);
		
		GenericCloudServiceConnectorFactory testFactory = new GenericCloudServiceConnectorFactory(id, null);
		testFactory.setServiceConnectorType(String.class);
		testFactory.setCloud(mockCloud);
		testFactory.afterPropertiesSet();
		
		assertNull(testFactory.getObject());
	}
}
