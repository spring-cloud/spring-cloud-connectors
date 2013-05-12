package org.springframework.cloud.service.common;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfo extends BaseServiceInfo {
	protected String database;
	
	public RelationalServiceInfo(String id, String host, int port, String database, String userName, String password) {
		super(id, host, port, userName, password);
		this.database = database;
	}
	
	@ServiceProperty(category="connection")
	public abstract String getUrl();
	
	@ServiceProperty(category="connection")
	public String getDatabase() {
		return database;
	}

}
