package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.RabbitServiceInfo;

import java.util.Map;

public class UserProvidedRabbitServiceInfoCreator extends UserProvidedServiceInfoCreator<RabbitServiceInfo> {

	public UserProvidedRabbitServiceInfoCreator() {
		super("amqp:");
	}

	public RabbitServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");

		String uri = (String) credentials.get("uri");

		return new RabbitServiceInfo(id, uri);
	}

}
