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
public class CloudFoundryConnectorAmqpServiceTest extends AbstractCloudFoundryConnectorTest {
	@Test
	public void rabbitServiceCreationWithTags() {
		String[] versions = {"2.0", "2.2"};
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
				        getRabbitServicePayloadWithTags(version, "rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
				        getRabbitServicePayloadWithTags(version, "rabbit-2", hostname, port, username, password, "q-2", "vhost2")));
		}

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "rabbit-1"));
		assertNotNull(getServiceInfo(serviceInfos, "rabbit-2"));
	}

    @Test
    public void rabbitServiceCreationWithoutTags() {
        String[] versions = {"1.0", "1.1"};
        for (String version : versions) {
            when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
                .thenReturn(getServicesPayload(
                        getRabbitServicePayloadWithoutTags(version, "rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
                        getRabbitServicePayloadWithoutTags(version, "rabbit-2", hostname, port, username, password, "q-2", "vhost2")));
        }

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        assertNotNull(getServiceInfo(serviceInfos, "rabbit-1"));
        assertNotNull(getServiceInfo(serviceInfos, "rabbit-2"));
    }

    @Test
    public void rabbitServiceCreationNoLabelNoTags() {
        String[] versions = {"1.0", "1.1"};
        for (String version : versions) {
            when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
                .thenReturn(getServicesPayload(
                        getRabbitServicePayloadNoLabelNoTags(version, "rabbit-1", hostname, port, username, password, "q-1", "vhost1"),
                        getRabbitServicePayloadNoLabelNoTags(version, "rabbit-2", hostname, port, username, password, "q-2", "vhost2")));
        }

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        assertNotNull(getServiceInfo(serviceInfos, "rabbit-1"));
        assertNotNull(getServiceInfo(serviceInfos, "rabbit-2"));
    }

    private String getRabbitServicePayloadWithoutTags(String version, String serviceName,
            String hostname, int port,
            String user, String password, String name,
            String vHost) {
        return getRabbitServicePayload("test-rabbit-info-with-label-no-tags.json", version, serviceName, 
                                       hostname, port, user, password, name, vHost);
    }

    private String getRabbitServicePayloadNoLabelNoTags(String version, String serviceName,
            String hostname, int port,
            String user, String password, String name,
            String vHost) {
        return getRabbitServicePayload("test-rabbit-info-no-label-no-tags.json", version, serviceName, 
                                       hostname, port, user, password, name, vHost);
    }
    
	private String getRabbitServicePayloadWithTags(String version, String serviceName,
                                			  String hostname, int port,
                                			  String user, String password, String name,
                                			  String vHost) {
		return getRabbitServicePayload("test-rabbit-info.json", version, serviceName, 
		                               hostname, port, user, password, name, vHost);
	}

    private String getRabbitServicePayload(String filename, String version, String serviceName,
                                           String hostname, int port,
                                           String user, String password, String name,
                                           String vHost) {
        String payload = readTestDataFile(filename);
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
