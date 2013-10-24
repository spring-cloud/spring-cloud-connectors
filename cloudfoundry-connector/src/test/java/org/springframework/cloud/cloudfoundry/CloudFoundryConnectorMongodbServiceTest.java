package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getMongoServicePayload;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getServicesPayload;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

public class CloudFoundryConnectorMongodbServiceTest extends AbstractCloudFactoryConnectorTest {
	@Test
	public void mongoServiceCreation() {
		String[] versions = {"2.0", "2.2"};
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getMongoServicePayload(version, "mongo-1", hostname, port, username, password, "inventory-1", "db"),
						getMongoServicePayload(version, "mongo-2", hostname, port, username, password, "inventory-2", "db")));
		}

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "mongo-1"));
		assertNotNull(getServiceInfo(serviceInfos, "mongo-2"));
	}

}
