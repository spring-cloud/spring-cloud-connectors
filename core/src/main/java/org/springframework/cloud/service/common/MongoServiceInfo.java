package org.springframework.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("mongo")
public class MongoServiceInfo extends UriBasedServiceInfo {
	public MongoServiceInfo(String id, String host, int port, String username, String password, String db) {
		super(id, "mongodb", host, port, username, password, db);
	}

	public MongoServiceInfo(String id, String uri) {
		super(id, uri);
	}
	
	@ServiceProperty(category="connection")
	public String getDatabase() {
		return getUriInfo().getPath();
	}
	
}
