package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.SmtpServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorSmtpServiceTest extends AbstractCloudFoundryConnectorTest {

	@Test
	public void smtpServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(getSmtpServicePayload("smtp-1", hostname, username, password)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		SmtpServiceInfo smtpServiceInfo = (SmtpServiceInfo) getServiceInfo(serviceInfos, "smtp-1");
		assertNotNull(smtpServiceInfo);
		assertEquals(hostname, smtpServiceInfo.getHost());
		assertEquals(587, smtpServiceInfo.getPort());
		assertEquals(username, smtpServiceInfo.getUserName());
		assertEquals(password, smtpServiceInfo.getPassword());
	}

	@Test
	public void smtpServiceCreationWithUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(getSmtpServicePayloadWithUri("smtp-1", hostname, port, username, password)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		SmtpServiceInfo smtpServiceInfo = (SmtpServiceInfo) getServiceInfo(serviceInfos, "smtp-1");
		assertNotNull(smtpServiceInfo);
		assertEquals(hostname, smtpServiceInfo.getHost());
		assertEquals(port, smtpServiceInfo.getPort());
		assertEquals(username, smtpServiceInfo.getUserName());
		assertEquals(password, smtpServiceInfo.getPassword());
	}

	private String getSmtpServicePayload(String serviceName, String hostname,
										 String user, String password) {
		String payload = readTestDataFile("test-smtp-info.json");
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$username", user);
		payload = payload.replace("$password", password);

		return payload;
	}

	private String getSmtpServicePayloadWithUri(String serviceName, String hostname, int port,
										 String user, String password) {
		String payload = readTestDataFile("test-smtp-info-uri.json");
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", String.valueOf(port));
		payload = payload.replace("$username", user);
		payload = payload.replace("$password", password);

		return payload;
	}
}
