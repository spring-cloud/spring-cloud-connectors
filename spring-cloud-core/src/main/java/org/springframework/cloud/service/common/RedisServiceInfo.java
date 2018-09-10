package org.springframework.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 *
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
@ServiceLabel("redis")
public class RedisServiceInfo extends UriBasedServiceInfo {

	public static final String REDIS_SCHEME = "redis";
	public static final String REDISS_SCHEME = "rediss";

	public RedisServiceInfo(String id, String host, int port, String password) {
		super(id, REDIS_SCHEME, host, port, null, password, null);
	}

	public RedisServiceInfo(String id, String uri) {
		super(id, uri);
	}
}
