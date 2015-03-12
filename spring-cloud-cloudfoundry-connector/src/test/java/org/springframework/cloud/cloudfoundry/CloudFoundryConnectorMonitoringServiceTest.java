package org.springframework.cloud.cloudfoundry;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MonitoringServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorMonitoringServiceTest extends AbstractCloudFoundryConnectorTest {

	@Test
	public void monitoringServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(getMonitoringServicePayload("monitoring-1")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "monitoring-1", MonitoringServiceInfo.class);
	}
	
	private String getMonitoringServicePayload(String serviceName) {
		String payload = readTestDataFile("test-monitoring-info.json");
		payload = payload.replace("$serviceName", serviceName);
		
		return payload;
	}
}
