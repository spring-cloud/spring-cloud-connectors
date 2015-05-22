package org.springframework.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class AmqpServiceInfoCreator extends CloudFoundryServiceInfoCreator<AmqpServiceInfo> {

	public AmqpServiceInfoCreator() {
		super(new Tags("rabbitmq"), AmqpServiceInfo.AMQP_SCHEME, AmqpServiceInfo.AMQPS_SCHEME);
	}

	@SuppressWarnings("unchecked")
	public AmqpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		Map<String,Object> credentials = getCredentials(serviceData);

		String id = getId(serviceData);

		String uri = getUriFromCredentials(credentials);
		String managementUri = getStringFromCredentials(credentials, "http_api_uri");

		if (credentials.containsKey("uris")) {
			List<String> uris = (List<String>) credentials.get("uris");
			List<String> managementUris = (List<String>) credentials.get("http_api_uris");
			return new AmqpServiceInfo(id, uri, managementUri, uris, managementUris);
		}

		return new AmqpServiceInfo(id, uri, managementUri);
	}

}
