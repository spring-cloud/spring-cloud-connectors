package org.springframework.cloud.heroku;

import static org.junit.Assert.assertEquals;

import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * @author Ramnivas Laddad
 */
public abstract class AbstractHerokuConnectorRelationalServiceTest extends AbstractHerokuConnectorTest {
	private String databaseType;

	public AbstractHerokuConnectorRelationalServiceTest(String databaseType) {
		this.databaseType = databaseType;
	}

	protected String getJdbcUrl(String name) {
		String jdbcUrlDatabaseType = databaseType;
		if (databaseType.equals("postgres")) {
			jdbcUrlDatabaseType = "postgresql";
		}

		return "jdbc:" + jdbcUrlDatabaseType + "://" + hostname + ":" + port + "/" + name +
				"?user=" + username + "&password=" + password;
	}

	protected String getRelationalServiceUrl(String name) {
		String template = "$databaseType://$username:$password@$host:$port/$database";

		return template.replace("$databaseType", databaseType).
				replace("$username", username).
				replace("$password", password).
				replace("$host", hostname).
				replace("$port", Integer.toString(port)).
				replace("$database", name);
	}

	protected void assertReleationServiceInfo(RelationalServiceInfo serviceInfo, String databaseName) {
		assertEquals(hostname, serviceInfo.getHost());
		assertEquals(port, serviceInfo.getPort());
		assertEquals(username, serviceInfo.getUserName());
		assertEquals(password, serviceInfo.getPassword());
		assertEquals(getJdbcUrl(databaseName), serviceInfo.getJdbcUrl());
	}
}
