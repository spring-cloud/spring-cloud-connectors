package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getServicesPayload;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getSmtpServicePayload;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.SmtpServiceInfo;

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
}
