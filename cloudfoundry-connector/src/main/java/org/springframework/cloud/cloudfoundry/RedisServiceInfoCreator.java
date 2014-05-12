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
		super("redis", "redis");

	}

	public RedisServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");

		String uri = (String) credentials.get("uri");
		if (uri == null) {
		    uri = (String) credentials.get("url");
		}
		if (uri == null) {
        		String host = (String) credentials.get("hostname");
        		Integer port = Integer.parseInt(credentials.get("port").toString());
        		String password = (String) credentials.get("password");
        
        		return new RedisServiceInfo(id, host, port, password);
		} else {
		    return new RedisServiceInfo(id, uri);
		}
	}

}
