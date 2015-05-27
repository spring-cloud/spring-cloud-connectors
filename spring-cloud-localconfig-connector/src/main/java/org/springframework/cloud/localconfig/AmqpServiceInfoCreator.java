package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 * @author Christopher Smith
 */
public class AmqpServiceInfoCreator extends LocalConfigServiceInfoCreator<AmqpServiceInfo> {

	public AmqpServiceInfoCreator() {
		super(AmqpServiceInfo.AMQP_SCHEME, AmqpServiceInfo.AMQPS_SCHEME);
	}

	@Override
	public AmqpServiceInfo createServiceInfo(String id, String uri) {
		return new AmqpServiceInfo(id, uri);
	}
}
