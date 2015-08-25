package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.SqlServerServiceInfo;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.service.common.RelationalServiceInfo.JDBC_PREFIX;
import static org.springframework.cloud.service.common.SqlServerServiceInfo.SQLSERVER_SCHEME;

public class CloudFoundryConnectorSqlServerServiceTest extends AbstractUserProvidedServiceInfoCreatorTest {

	private static final String INSTANCE_NAME = "database";
	private static final String SERVICE_NAME = "sqlserver-ups";

	@Test
	public void sqlServerServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayload(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, SQLSERVER_SCHEME + ":")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, SqlServerServiceInfo.class);
		assertJdbcUrlEqual(info, SQLSERVER_SCHEME, INSTANCE_NAME);
		assertUriBasedServiceInfoFields(info, SQLSERVER_SCHEME, hostname, port, username, password, INSTANCE_NAME);
	}

	@Test
	public void sqlServerServiceCreationWithNoUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayloadWithNoUri(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertNotNull(info);
		assertFalse(SqlServerServiceInfo.class.isAssignableFrom(info.getClass()));  // service was not detected as SQL-Server
	}

	@Test
	public void sqlServerServiceCreationWithJdbcUrl() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getSqlServerServicePayloadWithJdbcurl(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, SQLSERVER_SCHEME + ":")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, SqlServerServiceInfo.class);
		assertJdbcUrlEqual(info, SQLSERVER_SCHEME, INSTANCE_NAME);
		assertUriBasedServiceInfoFields(info, SQLSERVER_SCHEME, hostname, port, username, password, INSTANCE_NAME);
	}

	protected String getSqlServerServicePayloadWithJdbcurl(String serviceName, String hostname, int port,
														String user, String password, String name, String scheme) {
		String payload = getRelationalPayload("test-sqlserver-info-jdbc-url.json", serviceName,
				hostname, port, user, password, name);
		return payload.replace("$scheme", scheme);
	}

	protected String getJdbcUrl(String scheme, String name) {
		return String.format("%s%s://%s:%d;database=%s;user=%s;password=%s", JDBC_PREFIX, scheme, hostname, port, name, username, password);
	}
}
