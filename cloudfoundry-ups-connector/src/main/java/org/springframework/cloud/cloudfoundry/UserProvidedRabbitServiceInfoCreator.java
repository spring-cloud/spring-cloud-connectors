package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.AmqpServiceInfo;

import java.util.Map;

public class UserProvidedRabbitServiceInfoCreator extends UserProvidedServiceInfoCreator<AmqpServiceInfo> {

	public UserProvidedRabbitServiceInfoCreator() {
		super("amqp:");
	}

	public AmqpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");

		String uri = (String) credentials.get("uri");

		return new AmqpServiceInfo(id, uri);
	}

}
