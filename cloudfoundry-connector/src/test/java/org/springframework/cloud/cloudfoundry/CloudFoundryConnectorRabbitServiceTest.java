package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
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

	private String getRabbitServicePayload(String version, String serviceName,
                                			  String hostname, int port,
                                			  String user, String password, String name,
                                			  String vHost) {
		String payload = readTestDataFile("test-rabbit-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$user", user);
		payload = payload.replace("$pass", password);
		payload = payload.replace("$name", name);
		payload = payload.replace("$virtualHost", vHost);
		
		return payload;
	}

	
}
