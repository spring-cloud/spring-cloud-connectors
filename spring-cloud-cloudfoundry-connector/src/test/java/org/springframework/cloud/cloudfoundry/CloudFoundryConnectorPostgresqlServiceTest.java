package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.service.common.PostgresqlServiceInfo.POSTGRES_SCHEME;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.util.UriInfo;

/**
 * @author Ramnivas Laddad
 */
public class CloudFoundryConnectorPostgresqlServiceTest extends AbstractCloudFoundryConnectorRelationalServiceTest {

	@Test
	public void postgresqlServiceCreation() {
		String name1 = "database-1";
		String name2 = "database-2";
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getPostgresqlServicePayload("postgresql-1", hostname, port, username, password, name1),
						getPostgresqlServicePayload("postgresql-2", hostname, port, username, password, name2)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo info1 = getServiceInfo(serviceInfos, "postgresql-1");
		ServiceInfo info2 = getServiceInfo(serviceInfos, "postgresql-2");

		assertServiceFoundOfType(info1, PostgresqlServiceInfo.class);
		assertServiceFoundOfType(info2, PostgresqlServiceInfo.class);

		assertJdbcUrlEqual(info1, POSTGRES_SCHEME, name1);
		assertJdbcUrlEqual(info2, POSTGRES_SCHEME, name2);

		assertUriBasedServiceInfoFields(info1, POSTGRES_SCHEME, hostname, port, username, password, name1);
		assertUriBasedServiceInfoFields(info2, POSTGRES_SCHEME, hostname, port, username, password, name2);
	}

	@Test
	public void postgresqlWithSpecialCharsServiceCreation() {
		String userWithSpecialChars = "u%u:u+";
		String passwordWithSpecialChars = "p%p:p+";

		String name1 = "database-1";
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
				getServicesPayload(getPostgresqlServicePayload("postgresql-1", hostname,
						port, userWithSpecialChars, passwordWithSpecialChars, name1)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo info1 = getServiceInfo(serviceInfos, "postgresql-1");

		assertServiceFoundOfType(info1, PostgresqlServiceInfo.class);

		assertEquals(getJdbcUrl(hostname, port, userWithSpecialChars,
				passwordWithSpecialChars, name1),
				((RelationalServiceInfo) info1).getJdbcUrl());

		assertUriBasedServiceInfoFields(info1, POSTGRES_SCHEME, hostname, port,
				userWithSpecialChars, passwordWithSpecialChars, name1);
	}
	
	@Test
	public void postgresqlServiceCreationNoLabelNoTags() {
		String name1 = "database-1";
		String name2 = "database-2";
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getPostgresqlServicePayloadNoLabelNoTags("postgresql-1", hostname, port, username, password, name1),
						getPostgresqlServicePayloadNoLabelNoTags("postgresql-2", hostname, port, username, password, name2)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo info1 = getServiceInfo(serviceInfos, "postgresql-1");
		ServiceInfo info2 = getServiceInfo(serviceInfos, "postgresql-2");

		assertServiceFoundOfType(info1, PostgresqlServiceInfo.class);
		assertServiceFoundOfType(info2, PostgresqlServiceInfo.class);

		assertJdbcUrlEqual(info1, POSTGRES_SCHEME, name1);
		assertJdbcUrlEqual(info2, POSTGRES_SCHEME, name2);

		assertUriBasedServiceInfoFields(info1, POSTGRES_SCHEME, hostname, port, username, password, name1);
		assertUriBasedServiceInfoFields(info2, POSTGRES_SCHEME, hostname, port, username, password, name2);
	}

	@Test
	public void postgresqlServiceCreationWithJdbcUrl() {
		String name1 = "database-1";
		String name2 = "database-2";
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getPostgresqlServicePayloadWithJdbcUrl("postgresql-1", hostname, port, username, password, name1),
						getPostgresqlServicePayloadWithJdbcUrl("postgresql-2", hostname, port, username, password, name2)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo info1 = getServiceInfo(serviceInfos, "postgresql-1");
		ServiceInfo info2 = getServiceInfo(serviceInfos, "postgresql-2");

		assertServiceFoundOfType(info1, PostgresqlServiceInfo.class);
		assertServiceFoundOfType(info2, PostgresqlServiceInfo.class);

		assertJdbcUrlEqual(info1, POSTGRES_SCHEME, name1);
		assertJdbcUrlEqual(info2, POSTGRES_SCHEME, name2);

		assertUriBasedServiceInfoFields(info1, POSTGRES_SCHEME, hostname, port, username, password, name1);
		assertUriBasedServiceInfoFields(info2, POSTGRES_SCHEME, hostname, port, username, password, name2);
	}

	private String getPostgresqlServicePayload(String serviceName,
											   String hostname, int port,
											   String user, String password, String name) {
		return getRelationalPayload("test-postgresql-info.json", serviceName,
				hostname, port, user, password, name);
	}

	private String getPostgresqlServicePayloadNoLabelNoTags(String serviceName,
															String hostname, int port,
															String user, String password, String name) {
		return getRelationalPayload("test-postgresql-info-no-label-no-tags.json", serviceName,
				hostname, port, user, password, name);
	}

	private String getPostgresqlServicePayloadWithJdbcUrl(String serviceName,
															String hostname, int port,
															String user, String password, String name) {
		return getRelationalPayload("test-postgresql-info-jdbc-url.json", serviceName,
				hostname, port, user, password, name);
	}

	private String getJdbcUrl(String hostname, int port, String user, String password,
			String name) {
		return String.format("jdbc:postgresql://%s:%d/%s?user=%s&password=%s", hostname,
				port, name, UriInfo.urlEncode(user), UriInfo.urlEncode(password));
	}
}
