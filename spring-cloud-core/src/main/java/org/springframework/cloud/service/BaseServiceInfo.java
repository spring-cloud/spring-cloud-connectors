package org.springframework.cloud.service;

/**
 * Common class for all {@link ServiceInfo}s
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class BaseServiceInfo implements ServiceInfo {
	protected String id;
	protected String host;
	protected int port;
	protected String userName;
	protected String password;

	protected BaseServiceInfo(String id) {
		this.id = id;
	}
	
	public BaseServiceInfo(String id, String host, int port, String username, String password) {
		this(id);
		this.host = host;
		this.port = port;
		this.userName = username;
		this.password = password;
	}
	
	@ServiceProperty
	public String getId() {
		return id;
	}
	
	@ServiceProperty(category="connection")
	public String getUserName() {
		return userName;
	}
	
	@ServiceProperty(category="connection")
	public String getPassword() {
		return password;
	}

	@ServiceProperty(category="connection")
	public String getHost() {
		return host;
	}

	@ServiceProperty(category="connection")
	public int getPort() {
		return port;
	}
}
