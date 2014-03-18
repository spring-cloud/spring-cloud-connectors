package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserProvidedPostgresqlServiceInfoCreatorTest extends AbstractUserProvidedServiceInfoCreatorTest {

	private static final String INSTANCE_NAME = "database";
	private static final String POSTGRES_SCHEME = "postgres:";
	private static final String SERVICE_NAME = "postgres-ups";

	@Test
	public void postgresServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayload(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, POSTGRES_SCHEME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		PostgresqlServiceInfo info = (PostgresqlServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertNotNull(info);
		assertEquals(getJdbcUrl("postgresql", INSTANCE_NAME), info.getJdbcUrl());
	}

	@Test
	public void postgresServiceCreationWithNoUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayloadWithNoUri(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME)));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		BaseServiceInfo info = (BaseServiceInfo) getServiceInfo(serviceInfos, SERVICE_NAME);
		assertFalse(MysqlServiceInfo.class.isAssignableFrom(info.getClass()));  // service was not detected as MySQL
		assertNotNull(info);
	}
}
