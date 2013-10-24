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
public class CloudFoundryConnectorMonitoringServiceTest extends AbstractCloudFactoryConnectorTest {

	@Test
	public void monitoringServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(getMonitoringServicePayload("monitoring-1")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "monitoring-1"));
	}
	
	private String getMonitoringServicePayload(String serviceName) {
		String payload = readTestDataFile("test-monitoring-info.json");
		payload = payload.replace("$serviceName", serviceName);
		
		return payload;
	}
}
