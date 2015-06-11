package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.DB2ServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class CloudFoundryConnectorDB2ServiceTest extends AbstractUserProvidedServiceInfoCreatorTest {

	private static final String INSTANCE_NAME = "database";
	private static final String DB2_SCHEME = "db2:";
	private static final String SERVICE_NAME = "db2-ups";

	@Test
	public void db2ServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayload(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, DB2_SCHEME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		DB2ServiceInfo info = (DB2ServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, DB2ServiceInfo.class);
		assertEquals(getDB2JdbcUrl(INSTANCE_NAME), info.getJdbcUrl());
	}

	@Test
	public void db2ServiceCreationWithNoUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayloadWithNoUri(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		BaseServiceInfo info = (BaseServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertFalse(MysqlServiceInfo.class.isAssignableFrom(info.getClass()));  // service was not detected as MySQL
		assertNotNull(info);
	}

	@Test
	public void dServiceCreationWithJdbcUrl() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getDB2ServicePayloadWithJdbcurl(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, DB2_SCHEME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		DB2ServiceInfo info = (DB2ServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, DB2ServiceInfo.class);
		assertEquals(RelationalServiceInfo.JDBC_PREFIX + "db2:rawjdbcurl", info.getJdbcUrl());
	}

	protected String getDB2ServicePayloadWithJdbcurl(String serviceName, String hostname, int port,
														String user, String password, String name, String scheme) {
		String payload = getRelationalPayload("test-db2-info-jdbc-url.json", serviceName,
				hostname, port, user, password, name);
		return payload.replace("$scheme", scheme);
	}

	private String getDB2JdbcUrl(String name) {
		return String.format("jdbc:db2://%s:%d/%s:user=%s;password=%s;", hostname, port, name, username, password);
	}
}
