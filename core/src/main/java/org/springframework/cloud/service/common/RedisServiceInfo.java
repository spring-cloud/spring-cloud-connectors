package org.springframework.cloud.service.common;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("redis")
public class RedisServiceInfo extends BaseServiceInfo {
	public RedisServiceInfo(String id, String host, int port, String password) {
		super(id, "redis", host, port, null, password, null);
	}
}
