package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class AmqpServiceInfoCreator extends CloudFoundryServiceInfoCreator<AmqpServiceInfo> {

	public AmqpServiceInfoCreator() {
		super("rabbitmq", "amqp");
	}

	public AmqpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");
		
		String uri = (String) credentials.get("uri");
		if (uri == null || uri.length() == 0) {
			uri = (String) credentials.get("url");
		}
			
		return new AmqpServiceInfo(id, uri);
	}

}
