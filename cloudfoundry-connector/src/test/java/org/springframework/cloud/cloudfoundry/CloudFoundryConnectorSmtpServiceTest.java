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
public class CloudFoundryConnectorSmtpServiceTest extends AbstractCloudFactoryConnectorTest {

	@Test
	public void smtpServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(getSmtpServicePayload("n/a", "smtp-1", hostname, username, password)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		SmtpServiceInfo smptServiceInfo = (SmtpServiceInfo) getServiceInfo(serviceInfos, "smtp-1");
		assertNotNull(smptServiceInfo);
		assertEquals(hostname, smptServiceInfo.getHost());
		assertEquals(587, smptServiceInfo.getPort());
		assertEquals(username, smptServiceInfo.getUserName());
		assertEquals(password, smptServiceInfo.getPassword());		
	}

	private String getSmtpServicePayload(String version, String serviceName,
                                			String hostname, 
                                			String user, String password) {
		String payload = readTestDataFile("test-smtp-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$username", user);
		payload = payload.replace("$password", password);
		
		return payload;
	}
}
