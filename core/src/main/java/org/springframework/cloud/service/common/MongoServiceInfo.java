package org.springframework.cloud.service.common;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("mongo")
public class MongoServiceInfo extends BaseServiceInfo {
	private String database;

	public MongoServiceInfo(String id, String host, int port, String database, String userName, String password) {
		super(id, host, port, userName, password);
		this.database = database;
	}

	@ServiceProperty(category="connection")
	public String getDatabase() {
		return database;
	}
}
