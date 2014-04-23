package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author Ramnivas Laddad
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
	

}
