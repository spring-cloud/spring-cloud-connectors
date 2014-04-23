package org.springframework.cloud.heroku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorApplicationTest extends AbstractHerokuConnectorTest {

    @Test
	public void isInMatchingEnvironment() {
		when(mockEnvironment.getEnvValue("DYNO")).thenReturn("web.1");
		assertTrue(testCloudConnector.isInMatchingCloud());
		
		when(mockEnvironment.getEnvValue("DYNO")).thenReturn(null);
		assertFalse(testCloudConnector.isInMatchingCloud());
	}
	
	@Test
	public void applicationInstanceInfo() {
		when(mockEnvironment.getEnvValue("SPRING_CLOUD_APP_NAME")).thenReturn("myapp");
		when(mockEnvironment.getEnvValue("DYNO")).thenReturn("web.1");
		when(mockEnvironment.getEnvValue("PORT")).thenReturn(Integer.toString(port));
		when(mockEnvironment.getHost()).thenReturn(hostname);
		
		assertEquals("myapp", testCloudConnector.getApplicationInstanceInfo().getAppId());
		assertEquals("web.1", testCloudConnector.getApplicationInstanceInfo().getInstanceId());
		Map<String,Object> appProps = testCloudConnector.getApplicationInstanceInfo().getProperties();
		assertEquals(hostname, appProps.get("host"));
		assertEquals(Integer.toString(port), appProps.get("port"));
	}
	
	@Test
	public void applicationInstanceInfoNoSpringCloudAppName() {
		when(mockEnvironment.getEnvValue("DYNO")).thenReturn("web.1");
		when(mockEnvironment.getEnvValue("PORT")).thenReturn(Integer.toString(port));
		when(mockEnvironment.getHost()).thenReturn(hostname);
		assertEquals("<unknown>", testCloudConnector.getApplicationInstanceInfo().getAppId());
		assertEquals("web.1", testCloudConnector.getApplicationInstanceInfo().getInstanceId());
	}

}
