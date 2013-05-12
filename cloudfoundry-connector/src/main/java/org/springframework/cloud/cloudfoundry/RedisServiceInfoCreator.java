package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.RedisServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RedisServiceInfoCreator extends CloudFoundryServiceInfoCreator<RedisServiceInfo> {

	public RedisServiceInfoCreator() {
		super("redis");

	}

	public RedisServiceInfo createServiceInfo(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> serviceDataMap = (Map<String,Object>) serviceData;

		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
		
		String id = (String) serviceDataMap.get("name");
		
		String host = (String) credentials.get("hostname");
		Integer port = (Integer) credentials.get("port");
		String password = (String) credentials.get("password");

		return new RedisServiceInfo(id, host, port, password);
	}

}
