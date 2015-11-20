package org.springframework.cloud.heroku;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * @author Ramnivas Laddad
 */
public class HerokuConnectorPostgresqlServiceTest extends AbstractHerokuConnectorRelationalServiceTest {
	public HerokuConnectorPostgresqlServiceTest() {
		super(PostgresqlServiceInfo.POSTGRES_SCHEME);
	}

	@Test
	public void postgresqlServiceCreationPrimary() {
		assertPostgresServiceCreated("DATABASE_URL", "DATABASE");
	}

	@Test
	public void postgresqlServiceCreationSecondary() {
		assertPostgresServiceCreated("HEROKU_POSTGRESQL_YELLOW_URL", "HEROKU_POSTGRESQL_YELLOW");
	}

	private void assertPostgresServiceCreated(String envVarName, String serviceInstanceName) {
		Map<String, String> env = new HashMap<String, String>();
		String postgresUrl = getRelationalServiceUrl("db");
		env.put(envVarName, postgresUrl);
		when(mockEnvironment.getEnv()).thenReturn(env);

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo serviceInfo = getServiceInfo(serviceInfos, serviceInstanceName);
		assertNotNull(serviceInfo);
		assertTrue(serviceInfo instanceof PostgresqlServiceInfo);
		assertReleationServiceInfo((PostgresqlServiceInfo) serviceInfo, "db");
	}
}
