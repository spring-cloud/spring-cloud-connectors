package org.springframework.cloud.cloudfoundry;

import java.util.List;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

public class AbstractCloudFactoryConnectorTest {
	protected CloudFoundryConnector testCloudConnector = new CloudFoundryConnector();
	@Mock protected EnvironmentAccessor mockEnvironment;

	protected static final String hostname = "10.20.30.40";
	protected static final int port = 1234;
	protected static String username = "myuser";
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
	
	protected static String getJdbcUrl(String databaseType, String name) {
		String jdbcUrlDatabaseType = databaseType;
		if (databaseType.equals("postgres")) {
			jdbcUrlDatabaseType = "postgresql";
		}

		return "jdbc:" + jdbcUrlDatabaseType + "://" + hostname + ":" + port + "/" + name + "?user=" + username + "&password=" + password;
	}

}
