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
		super(new Tags("rabbitmq"), AmqpServiceInfo.URI_SCHEME);
	}

	public AmqpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");

		String id = (String) serviceData.get("name");

		String uri = getStringFromCredentials(credentials, "uri", "url");

		return new AmqpServiceInfo(id, uri);
	}

}
