package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.relational.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {

	public RelationalServiceInfoCreator(String label) {
		super(label);
	}

	public abstract SI createServiceInfo(String id, String host, int port, String database,
			String userName, String password);
	
	public SI createServiceInfo(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> serviceDataMap = (Map<String,Object>) serviceData;

		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
		
		String id = (String) serviceDataMap.get("name");
		
		String host = (String) credentials.get("hostname");
		Integer port = (Integer) credentials.get("port");
		String password = (String) credentials.get("password");
	
		String database = (String) credentials.get("name");
		String userName = (String) credentials.get("user");

		return createServiceInfo(id, host, port, database, userName, password);
		//return new MysqlServiceInfo(id, host, port, database, userName, password);
	}

}
