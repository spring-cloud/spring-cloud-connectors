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
        // the literal in the tag is CloudFoundry-specific
		super(new Tags("redis"), RedisServiceInfo.URI_SCHEME);
	}

	public RedisServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");

		String id = (String) serviceData.get("name");

		String uri = getStringFromCredentials(credentials, "uri", "url");

		if (uri == null) {
			String host = getStringFromCredentials(credentials, "hostname", "host");
			Integer port = Integer.parseInt(credentials.get("port").toString());
			String password = (String) credentials.get("password");

			return new RedisServiceInfo(id, host, port, password);
		} else {
			return new RedisServiceInfo(id, uri);
		}
	}

}
