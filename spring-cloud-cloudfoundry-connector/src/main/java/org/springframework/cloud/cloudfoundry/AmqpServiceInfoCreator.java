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
		super(new Tags("rabbitmq"), AmqpServiceInfo.AMQP_SCHEME, AmqpServiceInfo.AMQPS_SCHEME);
	}

	public AmqpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		Map<String,Object> credentials = getCredentials(serviceData);

		String id = (String) serviceData.get("name");

		String uri = getUriFromCredentials(credentials);

		return new AmqpServiceInfo(id, uri);
	}

}
