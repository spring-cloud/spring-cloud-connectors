package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getApplicationInstanceInfo;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudCoundryConnectorTest extends AbstractCloudFactoryConnectorTest {
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
	
}
