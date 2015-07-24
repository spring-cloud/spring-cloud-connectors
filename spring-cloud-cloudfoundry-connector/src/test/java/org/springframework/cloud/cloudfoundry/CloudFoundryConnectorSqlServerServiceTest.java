package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.service.common.SqlServerServiceInfo;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class CloudFoundryConnectorSqlServerServiceTest extends AbstractUserProvidedServiceInfoCreatorTest {

	private static final String INSTANCE_NAME = "database";
	private static final String SQLSERVER_SCHEME = "sqlserver:";
	private static final String SERVICE_NAME = "sqlserver-ups";

	@Test
	public void sqlServerServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayload(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, SQLSERVER_SCHEME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		SqlServerServiceInfo info = (SqlServerServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, SqlServerServiceInfo.class);
		assertEquals(getSqlServerJdbcUrl(INSTANCE_NAME), info.getJdbcUrl());
	}

	@Test
	public void sqlServerServiceCreationWithNoUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayloadWithNoUri(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		BaseServiceInfo info = (BaseServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertFalse(SqlServerServiceInfo.class.isAssignableFrom(info.getClass()));  // service was not detected as SQL-Server
		assertNotNull(info);
	}

	@Test
	public void sqlServerServiceCreationWithJdbcUrl() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getSqlServerServicePayloadWithJdbcurl(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, SQLSERVER_SCHEME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		SqlServerServiceInfo info = (SqlServerServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, SqlServerServiceInfo.class);
		assertEquals(RelationalServiceInfo.JDBC_PREFIX + "sqlserver:rawjdbcurl", info.getJdbcUrl());
	}

	protected String getSqlServerServicePayloadWithJdbcurl(String serviceName, String hostname, int port,
														String user, String password, String name, String scheme) {
		String payload = getRelationalPayload("test-sqlserver-info-jdbc-url.json", serviceName,
				hostname, port, user, password, name);
		return payload.replace("$scheme", scheme);
	}

	private String getSqlServerJdbcUrl(String name) {
		return "jdbc:sqlserver://" + hostname + ":" + port + ";database=" + name + ";user=" + username + ";password=" + password;
	}
}
