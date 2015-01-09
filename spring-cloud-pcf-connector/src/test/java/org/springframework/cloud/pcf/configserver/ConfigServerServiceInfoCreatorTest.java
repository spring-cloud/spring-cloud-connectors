package org.springframework.cloud.pcf.configserver;

import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.service.ServiceInfo;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Connector tests for Config Server services
 *
 * @author Chris Schaefer
 */
public class ConfigServerServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {
	private static final String CONFIG_SERVER_SERVICE_TAG_NAME = "myConfigServerService";
	private static final String VCAP_SERVICES_ENV_KEY = "VCAP_SERVICES";
	private static final String PAYLOAD_FILE_NAME = "test-config-server-info.json";
	private static final String PAYLOAD_TEMPLATE_SERVICE_NAME = "$serviceName";
	private static final String PAYLOAD_TEMPLATE_HOSTNAME = "$hostname";
	private static final String PAYLOAD_TEMPLATE_PORT = "$port";
	private static final String PAYLOAD_TEMPLATE_USER = "$user";
	private static final String PAYLOAD_TEMPLATE_PASS = "$pass";

	@Test
	public void configServerServiceCreationWithTags() {
		when(mockEnvironment.getEnvValue(VCAP_SERVICES_ENV_KEY))
				.thenReturn(getServicesPayload(getConfigServerServicePayload(CONFIG_SERVER_SERVICE_TAG_NAME,
						hostname, port, username, password)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, CONFIG_SERVER_SERVICE_TAG_NAME, ConfigServerServiceInfo.class);
	}

	private String getConfigServerServicePayload(String serviceName, String hostname, int port, String user, String password) {
		String payload = readTestDataFile(PAYLOAD_FILE_NAME);
		payload = payload.replace(PAYLOAD_TEMPLATE_SERVICE_NAME, serviceName);
		payload = payload.replace(PAYLOAD_TEMPLATE_HOSTNAME, hostname);
		payload = payload.replace(PAYLOAD_TEMPLATE_PORT, Integer.toString(port));
		payload = payload.replace(PAYLOAD_TEMPLATE_USER, user);
		payload = payload.replace(PAYLOAD_TEMPLATE_PASS, password);

		return payload;
	}
}
