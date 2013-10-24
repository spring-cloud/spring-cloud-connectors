package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getRabbitServicePayload;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getServicesPayload;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

public class CloudFoundryConnectorRabbitServiceTest extends AbstractCloudFactoryConnectorTest {
	@Test
	public void rabbitServiceCreation() {
		String[] versions = {"2.0", "2.2"};
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getRabbitServicePayload(version, "rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
						getRabbitServicePayload(version, "rabbit-2", hostname, port, username, password, "q-2", "vhost2")));
		}

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "rabbit-1"));
		assertNotNull(getServiceInfo(serviceInfos, "rabbit-2"));
	}

}
