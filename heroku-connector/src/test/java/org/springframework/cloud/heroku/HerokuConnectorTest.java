package org.springframework.cloud.heroku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.heroku.HerokuConnectorTestHelper.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorTest {
	private HerokuConnector testCloudConnector = new HerokuConnector();
	@Mock EnvironmentAccessor mockEnvironment;

	private static final String host = "10.20.30.40";
	private static final int port = 1234;
	private static String username = "myuser";
	private static final String password = "mypass";

	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testCloudConnector.setCloudEnvironment(mockEnvironment);
	}
	
	@Test
	public void isInMatchingEnvironment() {
		when(mockEnvironment.getValue("DYNO")).thenReturn("web.1");
		assertTrue(testCloudConnector.isInMatchingCloud());
		
		when(mockEnvironment.getValue("DYNO")).thenReturn(null);
		assertFalse(testCloudConnector.isInMatchingCloud());
	}
	
	@Test
	public void postgresqlServiceCreation() {
		Map<String, String> env = new HashMap<String, String>();
		String postgresUrl = createPostgresUrl(host, port, "db", username, password);
		env.put("HEROKU_POSTGRESQL_YELLOW_URL", postgresUrl);
		when(mockEnvironment.getEnv()).thenReturn(env);

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo serviceInfo = getServiceInfo(serviceInfos, "HEROKU_POSTGRESQL_YELLOW_URL");
		assertNotNull(serviceInfo);
		assertTrue(serviceInfo instanceof PostgresqlServiceInfo);
		PostgresqlServiceInfo postgresqlServiceInfo = (PostgresqlServiceInfo)serviceInfo;
		assertEquals(host, postgresqlServiceInfo.getHost());
		assertEquals(port, postgresqlServiceInfo.getPort());
		assertEquals(username, postgresqlServiceInfo.getUserName());
		assertEquals(password, postgresqlServiceInfo.getPassword());
		assertEquals("jdbc:" + postgresUrl, postgresqlServiceInfo.getJdbcUrl());
	}

	@Test
	public void applicationInstanceInfo() {
		when(mockEnvironment.getValue("SPRING_CLOUD_APP_NAME")).thenReturn("myapp");
		when(mockEnvironment.getValue("DYNO")).thenReturn("web.1");
		when(mockEnvironment.getValue("PORT")).thenReturn(Integer.toString(port));
		when(mockEnvironment.getHost()).thenReturn(host);
		
		assertEquals("myapp", testCloudConnector.getApplicationInstanceInfo().getAppId());
		assertEquals("web.1", testCloudConnector.getApplicationInstanceInfo().getInstanceId());
		Map<String,Object> appProps = testCloudConnector.getApplicationInstanceInfo().getProperties();
		assertEquals(host, appProps.get("host"));
		assertEquals(Integer.toString(port), appProps.get("port"));
	}
	
	@Test
	public void applicationInstanceInfoNoSpringCloudAppName() {
		when(mockEnvironment.getValue("DYNO")).thenReturn("web.1");
		when(mockEnvironment.getValue("PORT")).thenReturn(Integer.toString(port));
		when(mockEnvironment.getHost()).thenReturn(host);
		assertEquals("<unknown>", testCloudConnector.getApplicationInstanceInfo().getAppId());
		assertEquals("web.1", testCloudConnector.getApplicationInstanceInfo().getInstanceId());
	}

	private static ServiceInfo getServiceInfo(List<ServiceInfo> serviceInfos, String serviceId) {
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getId().equals(serviceId)) {
				return serviceInfo;
			}
		}
		return null;
	}
	

}
