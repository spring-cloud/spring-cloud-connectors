package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.DB2ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.util.UriInfo;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.service.common.DB2ServiceInfo.DB2_SCHEME;
import static org.springframework.cloud.service.common.RelationalServiceInfo.JDBC_PREFIX;

public class CloudFoundryConnectorDB2ServiceTest extends AbstractUserProvidedServiceInfoCreatorTest {

	private static final String INSTANCE_NAME = "database";
	private static final String SERVICE_NAME = "db2-ups";

	@Test
	public void db2ServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayload(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, DB2_SCHEME + ":")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, DB2ServiceInfo.class);
		assertJdbcUrlEqual(info, DB2_SCHEME, INSTANCE_NAME);
		assertUriBasedServiceInfoFields(info, DB2_SCHEME, hostname, port, username, password, INSTANCE_NAME);
	}

	@Test
	public void db2ServiceCreationWithSpecialChars() {
		String userWithSpecialChars = "u%u:u+";
		String passwordWithSpecialChars = "p%p:p+";
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayload(SERVICE_NAME, hostname, port, userWithSpecialChars, 
								passwordWithSpecialChars, INSTANCE_NAME, DB2_SCHEME + ":")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, DB2ServiceInfo.class);
		assertEquals(getJdbcUrl("db2", hostname, port, INSTANCE_NAME, userWithSpecialChars, passwordWithSpecialChars), ((RelationalServiceInfo) info).getJdbcUrl());
		assertUriBasedServiceInfoFields(info, DB2_SCHEME, hostname, port, userWithSpecialChars, passwordWithSpecialChars, INSTANCE_NAME);
	}

	@Test
	public void db2ServiceCreationWithNoUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getUserProvidedServicePayloadWithNoUri(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertNotNull(info);
		assertFalse(MysqlServiceInfo.class.isAssignableFrom(info.getClass()));  // service was not detected as MySQL
		assertNotNull(info);
	}

	@Test
	public void dServiceCreationWithJdbcUrl() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getDB2ServicePayloadWithJdbcurl(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, DB2_SCHEME + ":")));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, DB2ServiceInfo.class);
		assertJdbcUrlEqual(info, DB2_SCHEME, INSTANCE_NAME);
		assertUriBasedServiceInfoFields(info, DB2_SCHEME, hostname, port, username, password, INSTANCE_NAME);
	}

	protected String getDB2ServicePayloadWithJdbcurl(String serviceName, String hostname, int port,
														String user, String password, String name, String scheme) {
		String payload = getRelationalPayload("test-db2-info-jdbc-url.json", serviceName,
				hostname, port, user, password, name);
		return payload.replace("$scheme", scheme);
	}

	protected String getJdbcUrl(String scheme, String name) {
		return String.format("%s%s://%s:%d/%s:user=%s;password=%s;", JDBC_PREFIX, scheme, hostname, port, name, 
				UriInfo.urlEncode(username), UriInfo.urlEncode(password));
	}

	private String getJdbcUrl(String scheme, String hostname, int port, String name, String user, String password) {
		return String.format("%s%s://%s:%d/%s:user=%s;password=%s;", JDBC_PREFIX, scheme, hostname, port, name, 
				UriInfo.urlEncode(user), UriInfo.urlEncode(password));
	}
}
