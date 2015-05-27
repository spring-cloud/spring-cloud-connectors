package org.springframework.cloud.heroku;

import java.util.List;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

/**
 * Base test class that provides setup and utility methods to generate test payload
 *
 * @author Ramnivas Laddad
 */
public abstract class AbstractHerokuConnectorTest {
	protected HerokuConnector testCloudConnector = new HerokuConnector();
	@Mock
	protected EnvironmentAccessor mockEnvironment;

	protected static final String hostname = "10.20.30.40";
	protected static final int port = 1234;
	protected static final String username = "myuser";
	protected static final String password = "mypass";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testCloudConnector.setCloudEnvironment(mockEnvironment);
	}

	protected static ServiceInfo getServiceInfo(List<ServiceInfo> serviceInfos, String serviceId) {
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getId().equals(serviceId)) {
				return serviceInfo;
			}
		}
		return null;
	}
}
