package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorAmqpServiceTest extends AbstractCloudFoundryConnectorTest {
	protected static final String hostname2 = "11.21.31.41";

	@Test
	public void rabbitServiceCreationWithTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(
					getRabbitServicePayloadWithTags("rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
					getRabbitServicePayloadWithTags("rabbit-2", hostname, port, username, password, "q-2", "vhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "rabbit-2", AmqpServiceInfo.class);
	}

	@Test
	public void rabbitServiceCreationWithManagementUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getRabbitServicePayloadWithTags("rabbit-1", hostname, port, username, password, "q-1", "vhost1")));

		String expectedManagementUri = "http://" + username + ":" + password + "@" + hostname + "/api";

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		AmqpServiceInfo amqpServiceInfo = (AmqpServiceInfo) serviceInfos.get(0);
		assertEquals(amqpServiceInfo.getManagementUri(), expectedManagementUri);
	}

	@Test
	public void rabbitServiceCreationWithoutManagementUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getRabbitServicePayloadNoLabelNoTags("rabbit-1", hostname, port, username, password, "q-1", "vhost1")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		AmqpServiceInfo amqpServiceInfo = (AmqpServiceInfo) serviceInfos.get(0);
		assertNull(amqpServiceInfo.getManagementUri());
	}

	@Test
	public void rabbitServiceCreationWithoutTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getRabbitServicePayloadWithoutTags("rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
						getRabbitServicePayloadWithoutTags("rabbit-2", hostname, port, username, password, "q-2", "vhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "rabbit-2", AmqpServiceInfo.class);
	}

	@Test
	public void rabbitServiceCreationMultipleUris() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(
					getRabbitServicePayloadMultipleUris("rabbit-1", hostname, hostname2, port, username, password, "q-1", "v%2Fhost1"),
					getRabbitServicePayloadMultipleUris("rabbit-2", hostname, hostname2, port, username, password, "q-2", "v%2Fhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "rabbit-2", AmqpServiceInfo.class);

		AmqpServiceInfo amqpServiceInfo = (AmqpServiceInfo) serviceInfos.get(0);
		assertNotNull(amqpServiceInfo.getUri());
		assertTrue(amqpServiceInfo.getUri().contains(hostname));
		assertEquals("v/host1", amqpServiceInfo.getVirtualHost());
		assertNotNull(amqpServiceInfo.getManagementUri());
		assertTrue(amqpServiceInfo.getManagementUri().contains(hostname));

		assertNotNull(amqpServiceInfo.getUris());
		assertEquals(2, amqpServiceInfo.getUris().size());
		assertTrue(amqpServiceInfo.getUris().get(0).contains(hostname));
		assertTrue(amqpServiceInfo.getUris().get(1).contains(hostname2));

		assertNotNull(amqpServiceInfo.getManagementUris());
		assertEquals(2, amqpServiceInfo.getManagementUris().size());
		assertTrue(amqpServiceInfo.getManagementUris().get(0).contains(hostname));
		assertTrue(amqpServiceInfo.getManagementUris().get(1).contains(hostname2));
	}

	@Test
	public void rabbitServiceCreationNoLabelNoTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(
					getRabbitServicePayloadNoLabelNoTags("rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
					getRabbitServicePayloadNoLabelNoTags("rabbit-2", hostname, port, username, password, "q-2", "vhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "rabbit-2", AmqpServiceInfo.class);
	}

	@Test
	public void rabbitServiceCreationNoLabelNoTagsSecure() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(
					getRabbitServicePayloadNoLabelNoTagsSecure("rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
					getRabbitServicePayloadNoLabelNoTagsSecure("rabbit-2", hostname, port, username, password, "q-2", "vhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "rabbit-1", AmqpServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "rabbit-2", AmqpServiceInfo.class);
	}

	@Test
	public void qpidServiceCreationNoLabelNoTags() throws Exception {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(
					getQpidServicePayloadNoLabelNoTags("qpid-1", hostname, port, username, password, "q-1", "vhost1"),
					getQpidServicePayloadNoLabelNoTags("qpid-2", hostname, port, username, password, "q-2", "vhost2")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "qpid-1", AmqpServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "qpid-2", AmqpServiceInfo.class);
		AmqpServiceInfo serviceInfo = (AmqpServiceInfo) getServiceInfo(serviceInfos, "qpid-1");
		assertNotNull(serviceInfo);
		assertEquals(username, serviceInfo.getUserName());
		assertEquals(password, serviceInfo.getPassword());
		assertEquals("vhost1", serviceInfo.getVirtualHost());
		URI uri = new URI(serviceInfo.getUri());
		assertTrue(uri.getQuery().contains("tcp://" + hostname + ":" + port));
	}

	private String getRabbitServicePayloadWithoutTags(String serviceName,
													  String hostname, int port,
													  String user, String password, String name,
													  String vHost) {
		return getAmqpServicePayload("test-rabbit-info-with-label-no-tags.json", serviceName,
				hostname, port, user, password, name, vHost);
	}

	private String getRabbitServicePayloadNoLabelNoTags(String serviceName,
														String hostname, int port,
														String user, String password, String name,
														String vHost) {
		return getAmqpServicePayload("test-rabbit-info-no-label-no-tags.json", serviceName,
				hostname, port, user, password, name, vHost);
	}

	private String getRabbitServicePayloadNoLabelNoTagsSecure(String serviceName,
														String hostname, int port,
														String user, String password, String name,
														String vHost) {
		return getAmqpServicePayload("test-rabbit-info-no-label-no-tags-secure.json", serviceName,
				hostname, port, user, password, name, vHost);
	}

	private String getRabbitServicePayloadWithTags(String serviceName,
												   String hostname, int port,
												   String user, String password, String name,
												   String vHost) {
		return getAmqpServicePayload("test-rabbit-info.json", serviceName,
				hostname, port, user, password, name, vHost);
	}

	private String getRabbitServicePayloadMultipleUris(String serviceName,
												   String hostname, String hostname2, int port,
												   String user, String password, String name,
												   String vHost) {
		String payload = getAmqpServicePayload("test-rabbit-info-multiple-uris.json", serviceName,
				hostname, port, user, password, name, vHost);
		payload = payload.replace("$host1", hostname);
		payload = payload.replace("$host2", hostname2);
		return payload;
	}

	private String getQpidServicePayloadNoLabelNoTags(String serviceName,
												   String hostname, int port,
												   String user, String password, String name,
												   String vHost) {
		return getAmqpServicePayload("test-qpid-info.json", serviceName,
				hostname, port, user, password, name, vHost);
	}

	private String getAmqpServicePayload(String filename, String serviceName,
										   String hostname, int port,
										   String user, String password, String name,
										   String vHost) {
		String payload = readTestDataFile(filename);
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
