package org.springframework.cloud.heroku;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractHerokuConnectorRelationalServiceTest extends AbstractHerokuConnectorTest {
	protected static String getJdbcUrl(String databaseType, String name) {
		String jdbcUrlDatabaseType = databaseType;
		if (databaseType.equals("postgres")) {
			jdbcUrlDatabaseType = "postgresql";
		}

		return "jdbc:" + jdbcUrlDatabaseType + "://" + hostname + ":" + port + "/" + name + 
			   "?user=" + username + "&password=" + password;
	}

    protected static String getRelationalServiceUrl(String databaseType, String name) {
        String template = "$databaseType://$username:$password@$host:$port/$database";

        return template.replace("$databaseType", databaseType).
                        replace("$username", username).
                        replace("$password", password).
                        replace("$host", hostname).
                        replace("$port", Integer.toString(port)).
                        replace("$database", name);
    }	
}
