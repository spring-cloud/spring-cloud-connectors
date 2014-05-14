package org.springframework.cloud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.CloudTestUtil.StubCloudConnector;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFactoryTest {

	@Test
	public void cloudConnectorRegistration() {
		CloudFactory cloudFactory = new CloudFactory();
		List<CloudConnector> registeredConnectors = cloudFactory.getCloudConnectors();
		
		assertEquals("One connector registered", 1, registeredConnectors.size());
		assertEquals("Registered connector is stub connector", StubCloudConnector.class, registeredConnectors.get(0).getClass());
	}

	@Test
	public void cloudServiceConnectorCreatorRegistration() {
		CloudFactory cloudFactory = new CloudFactory();
		List<ServiceConnectorCreator<?, ? extends ServiceInfo>> registeredServiceConnectorCreators = cloudFactory.getServiceCreators();
		
		assertEquals("One serviceCreators registered", 2, registeredServiceConnectorCreators.size());
		assertEquals("First registered connector is a stub", StubServiceConnectorCreator.class, 
				registeredServiceConnectorCreators.get(0).getClass());
		assertEquals("Second egistered connector is a non-parameterized stub", NonParameterizedStubServiceConnectorCreator.class, 
				registeredServiceConnectorCreators.get(1).getClass());
	}
	
	@Test
	public void cloudRetriveal() {
		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();
		
		assertNotNull(cloud);
	}
}
