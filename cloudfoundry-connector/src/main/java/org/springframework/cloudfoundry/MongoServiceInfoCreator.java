package org.springframework.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.document.MongoServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoServiceInfoCreator extends CloudFoundryServiceInfoCreator<MongoServiceInfo> {

	public MongoServiceInfoCreator() {
		super("mongodb");

	}

	public MongoServiceInfo createServiceInfo(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> serviceDataMap = (Map<String,Object>) serviceData;

		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
		
		String id = (String) serviceDataMap.get("name");
		
		String host = (String) credentials.get("hostname");
		Integer port = (Integer) credentials.get("port");
		String userName = (String) credentials.get("username");
		String password = (String) credentials.get("password");
		String database = (String) credentials.get("db");

		return new MongoServiceInfo(id, host, port, database, userName, password);
	}

}
