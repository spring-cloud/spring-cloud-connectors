package org.springframework.cloud.cloudfoundry;

import org.mockito.internal.matchers.InstanceOf;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractCloudFoundryConnectorRelationalServiceTest extends AbstractCloudFoundryConnectorTest {
	protected String getRelationalPayload(String templateFile, String serviceName,
			                              String hostname, int port, String user, String password, String name) {
		String payload = readTestDataFile(templateFile);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$user", user);
		payload = payload.replace("$password", password);
		payload = payload.replace("$name", name);

		return payload;
	}

	protected void assertJdbcUrlEqual(ServiceInfo serviceInfo, String scheme, String name) {
		assertThat(serviceInfo, new InstanceOf(RelationalServiceInfo.class));
		assertEquals(getJdbcUrl(scheme, name), ((RelationalServiceInfo) serviceInfo).getJdbcUrl());
	}

	protected String getJdbcUrl(String databaseType, String name) {
	    // this should be cleaned up more broadly; pull into RelationalServiceInfo interface?
		String jdbcUrlDatabaseType = databaseType;
		if (databaseType.equals("postgres")) {
			jdbcUrlDatabaseType = "postgresql";
		}

		return "jdbc:" + jdbcUrlDatabaseType + "://" + hostname + ":" + port + "/" + name +
			   "?user=" + username + "&password=" + password;
	}
}
