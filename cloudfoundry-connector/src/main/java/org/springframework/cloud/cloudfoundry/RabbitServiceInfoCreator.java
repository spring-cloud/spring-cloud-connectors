package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.RabbitServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RabbitServiceInfoCreator extends CloudFoundryServiceInfoCreator<RabbitServiceInfo> {

	public RabbitServiceInfoCreator() {
		super("rabbitmq");

	}

	public RabbitServiceInfo createServiceInfo(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> serviceDataMap = (Map<String,Object>) serviceData;

		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
		
		String id = (String) serviceDataMap.get("name");
		
		String uri = (String) credentials.get("uri");

		return new RabbitServiceInfo(id, uri);
	}

}
