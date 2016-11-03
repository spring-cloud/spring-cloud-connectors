package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class CloudFoundryConnectorApplicationTest extends AbstractCloudFoundryConnectorTest {
	
	@Test
	public void isInMatchingEnvironment() {
		when(mockEnvironment.getEnvValue("VCAP_APPLICATION")).thenReturn(getApplicationInstanceInfo("myapp", "http://myapp.com"));
		assertTrue(testCloudConnector.isInMatchingCloud());
		
		when(mockEnvironment.getEnvValue("VCAP_APPLICATION")).thenReturn(null);
		assertFalse(testCloudConnector.isInMatchingCloud());
	}
	
	@Test
	public void applicationInstanceInfo() {
		when(mockEnvironment.getEnvValue("VCAP_APPLICATION")).thenReturn(getApplicationInstanceInfo("my-app", "foo.cf.com", "bar.cf.com"));
		
		assertEquals("my-app", testCloudConnector.getApplicationInstanceInfo().getAppId());
		assertEquals(Arrays.asList("foo.cf.com", "bar.cf.com"), testCloudConnector.getApplicationInstanceInfo().getProperties().get("uris"));
	}
	
	private String getApplicationInstanceInfo(String name, String... uri) {
		String payload = readTestDataFile("test-application-info.json");
		payload = payload.replace("$name", name);
		StringBuilder uris = new StringBuilder();
		for (String u : uri) {
			if (uris.length() > 0) {
				uris.append(",");
			}
			uris.append("\"");
			uris.append(u);
			uris.append("\"");
		}
		payload = payload.replace("$uris", uris.toString());
		
		return payload;
	}

	@Test
	public void servicesInfosWithNullServices() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(null);
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(serviceInfos);
		assertEquals(0, serviceInfos.size());
	}

	@Test
	public void servicesInfosWithEmptyServices() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn("");
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(serviceInfos);
		assertEquals(0, serviceInfos.size());
	}

	@Test(expected = CloudException.class)
	public void servicesInfosWithNonJsonServices() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn("some value");
		testCloudConnector.getServiceInfos();
	}

	@Test
	public void serviceInfosWithMissingCredentials() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).
				thenReturn(getServicesPayload(readTestDataFile("test-credentials-missing.json")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(serviceInfos);
		assertEquals(1, serviceInfos.size());
	}

	@Test
	public void serviceInfosWithEmptyCredentials() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).
				thenReturn(getServicesPayload(readTestDataFile("test-credentials-empty.json")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(serviceInfos);
		assertEquals(1, serviceInfos.size());
	}

}
