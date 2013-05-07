package org.springframework.cloud;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class StubServiceConnector {
	public String id;
	public String host;
	public int port;
	public String username;
	public String password;
	public String config;
	
	// Parameters only for testing purpose. Normal service connector (such as a DataSource) may not have such
	// information 
	public StubServiceConnector(String id, String host, int port, String username, String password, String config) {
		this.id = id;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.config = config;
	}
}