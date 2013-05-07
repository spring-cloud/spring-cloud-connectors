package org.springframework.cloud.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
public abstract class AbstractCloudServiceConnectorFactoryTest<SCF extends AbstractCloudServiceConnectorFactory<SC>, SC, SI extends ServiceInfo> {
	@Mock Cloud mockCloud;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void creatingBoundService() throws Exception {
		String id = "my-service";
		
		when(mockCloud.getServiceConnector(id, getConnectorType(), null)).thenReturn(getMockConnector());
		
		SCF testSCF = createTestCloudServiceConnectorFactory(id, null);
		testSCF.setCloud(mockCloud);
		testSCF.afterPropertiesSet();
		
		assertSame(getMockConnector(), testSCF.getObject());
	}
	
	@Test
	public void creatingNonExistingServiceShouldFail() throws Exception {
		String id = "my-service";
		when(mockCloud.getServiceConnector(id, getConnectorType(), null)).thenReturn(null);
		
		SCF testSCF = createTestCloudServiceConnectorFactory(id, null);
		testSCF.setCloud(mockCloud);
		testSCF.afterPropertiesSet();

		assertNull(testSCF.getObject());
	}
	
	@Test
	public void creatingServiceConnectorWithoutIdReturnsTheSignletonService() throws Exception {
		String id = "my-service";
		
		List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
		serviceInfos.add(getTestServiceInfo(id));
		
		when(mockCloud.getServiceConnector(id, getConnectorType(), null)).thenReturn(getMockConnector());
		when(mockCloud.getServiceInfos(getConnectorType())).thenReturn(serviceInfos);
		
		SCF testSCF = createTestCloudServiceConnectorFactory(null, null);
		testSCF.setCloud(mockCloud);
		testSCF.afterPropertiesSet();

		assertSame(getMockConnector(), testSCF.getObject());
	}

	public abstract SCF createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config);
	
	public abstract Class<SC> getConnectorType();
	
	public abstract SC getMockConnector();
	
	public abstract SI getTestServiceInfo(String id);
}
