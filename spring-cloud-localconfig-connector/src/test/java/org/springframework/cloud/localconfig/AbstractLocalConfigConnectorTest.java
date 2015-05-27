package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;

public class AbstractLocalConfigConnectorTest {

    public static final String PROPERTIES_FILE = "localconfig.testuris.properties";

    protected StubbedOpenFileLocalConfigConnector connector = new StubbedOpenFileLocalConfigConnector();

	protected PassthroughEnvironmentAccessor env;

	protected static final String HOSTNAME = "10.20.30.40";
	protected static final int PORT = 1234;
	protected static final String USERNAME = "myuser";
	protected static final String PASSWORD = "mypass";

	@Before
	public void init() throws IOException {
		env = new PassthroughEnvironmentAccessor();
		env.setSystemProperty("spring.cloud.baz", "inline!");
		connector.setEnvironmentAccessor(env);
	}

	protected static ServiceInfo getServiceInfo(List<ServiceInfo> serviceInfos, String serviceId) {
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getId().equals(serviceId)) {
				return serviceInfo;
			}
		}
		return null;
	}

	protected static void assertUriParameters(UriBasedServiceInfo serviceInfo) {
		assertEquals(HOSTNAME, serviceInfo.getHost());
		assertEquals(PORT, serviceInfo.getPort());
		assertEquals(USERNAME, serviceInfo.getUserName());
		assertEquals(PASSWORD, serviceInfo.getPassword());
	}
}
