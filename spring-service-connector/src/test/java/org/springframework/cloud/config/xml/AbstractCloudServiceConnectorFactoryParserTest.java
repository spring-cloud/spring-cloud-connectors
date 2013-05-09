package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.StubCloudConnectorTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractCloudServiceConnectorFactoryParserTest<SC> extends StubCloudConnectorTest {

	protected abstract ServiceInfo createService(String id);
	
	protected abstract String getWithServiceIdContextFileName();
	
	protected abstract String getWithoutServiceIdContextFileName();

	protected abstract Class<SC> getConnectorType();
	
	// Tests where <cloud:data-source id="some-id" ...> form is used
	@Test
	public void cloudConnectorWithServiceNameSpecified_UniqueServiceExists() {
		ApplicationContext testContext = getTestApplicationContext(getWithServiceIdContextFileName(),
				createService("my-service"));
		
		assertNotNull("Getting service by type", testContext.getBean(getConnectorType()));
		assertNotNull("Getting service by id", testContext.getBean("my-service", getConnectorType()));		
	}

	@Test(expected=CloudException.class)
	public void cloudConnectorWithServiceNameSpecified_NoServiceExists_byType() {
		ApplicationContext testContext = getTestApplicationContext(getWithServiceIdContextFileName());
		
		testContext.getBean(getConnectorType());
	}

	@Test(expected=CloudException.class)
	public void cloudConnectorWithServiceNameSpecified_NoServiceExists_byId() {
		ApplicationContext testContext = getTestApplicationContext(getWithServiceIdContextFileName());
		
		testContext.getBean("my-service", getConnectorType());		
	}

	@Test
	public void cloudConnectorWithServiceNameSpecified_TwoServiceExist() {
		ApplicationContext testContext = getTestApplicationContext(getWithServiceIdContextFileName(),
				createService("my-service"), createService("my-service-2"));
		
		assertNotNull("Getting service by id", testContext.getBean("my-service", getConnectorType()));
		assertNotNull("Getting service by type", testContext.getBean(getConnectorType()));
	}
	
	
	// Tests where <cloud:data-source ...> form is used. 
	// Since id is NOT specified, getting a service will work only if there is a unique relational service bound
	// In unique services case, the id of the bean must match the service id.
	@Test
	public void cloudConnectorWithoutServiceNameSpecified_UniqueServiceExists() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createService("my-service"));
		
		assertNotNull("Getting service by id", testContext.getBean("my-service", getConnectorType()));		
		assertNotNull("Getting service by type", testContext.getBean(getConnectorType()));
	}
	
	@Test(expected=CloudException.class)
	public void cloudConnectorWithoutServiceNameSpecified_NoServiceExists_byType() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName());
		
		testContext.getBean(getConnectorType());
	}

	@Test(expected=CloudException.class)
	public void cloudConnectorWithoutServiceNameSpecified_NoServiceExists_byId() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName());
		
		testContext.getBean("my-service", getConnectorType());		
	}

	@Test(expected=CloudException.class)
	public void cloudConnectorWithoutServiceNameSpecified_TwoServiceExist_byType() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createService("my-service"), createService("my-service-2"));
		
		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected=CloudException.class)
	public void cloudConnectorWithoutServiceNameSpecified_TwoServiceExist_byId() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createService("my-service"), createService("my-service-2"));
		
		testContext.getBean(getConnectorType());
	}

}
