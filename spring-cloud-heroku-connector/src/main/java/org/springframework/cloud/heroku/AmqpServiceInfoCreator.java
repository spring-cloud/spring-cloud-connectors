package org.springframework.cloud.heroku;

import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class AmqpServiceInfoCreator extends HerokuServiceInfoCreator<AmqpServiceInfo> {

	public AmqpServiceInfoCreator() {
		super(AmqpServiceInfo.URI_SCHEME);
	}

	@Override
	public AmqpServiceInfo createServiceInfo(String id, String uri) {
		return new AmqpServiceInfo(HerokuUtil.computeServiceName(id), uri);
	}

    @Override
    public String[] getEnvPrefixes() {
        return new String[]{ "CLOUDAMQP_URL" };
    }
}
