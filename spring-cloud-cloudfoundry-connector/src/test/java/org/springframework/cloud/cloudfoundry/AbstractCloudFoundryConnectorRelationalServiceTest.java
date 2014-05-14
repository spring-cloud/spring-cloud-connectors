package org.springframework.cloud.cloudfoundry;

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

	protected static String getJdbcUrl(String databaseType, String name) {
		String jdbcUrlDatabaseType = databaseType;
		if (databaseType.equals("postgres")) {
			jdbcUrlDatabaseType = "postgresql";
		}

		return "jdbc:" + jdbcUrlDatabaseType + "://" + hostname + ":" + port + "/" + name + 
			   "?user=" + username + "&password=" + password;
	}

}
