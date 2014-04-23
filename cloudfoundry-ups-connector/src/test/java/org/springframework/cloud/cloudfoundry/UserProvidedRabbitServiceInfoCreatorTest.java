package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserProvidedRabbitServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {
	@Test
	public void rabbitServiceCreationWithTags() {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
					.thenReturn(getServicesPayload(
							getRabbitServicePayload("rabbit", hostname, port, username, password, "vhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "rabbit"));
	}

	private String getRabbitServicePayload(String serviceName,
										   String hostname, int port,
										   String user, String password,
										   String vHost) {
		return getRabbitServicePayload("test-ups-info.json", serviceName,
				hostname, port, user, password, vHost);
	}

	private String getRabbitServicePayload(String filename, String serviceName,
										   String hostname, int port,
										   String user, String password,
										   String vHost) {
		String payload = readTestDataFile(filename);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$user", user);
		payload = payload.replace("$pass", password);
		payload = payload.replace("$name", vHost);

		return payload;
	}

}
