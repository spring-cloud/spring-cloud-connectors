package org.springframework.cloud.heroku;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorTestHelper {
	public static String createPostgresUrl(String host, int port, String database, String username, String password) {
		String template = "postgres://$username:$password@$host:$port/$database";
		
		return template.replace("$username", username).
					    replace("$password", password).
					    replace("$host", host).
					    replace("$port", Integer.toString(port)).
					    replace("$database", database);
	}
}
