package org.springframework.cloud.heroku;

import org.springframework.cloud.service.common.RedisServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class RedisServiceInfoCreator extends HerokuServiceInfoCreator<RedisServiceInfo> {

	public RedisServiceInfoCreator() {
		super(RedisServiceInfo.URI_SCHEME);
	}

	@Override
	public RedisServiceInfo createServiceInfo(String id, String uri) {
		return new RedisServiceInfo(HerokuUtil.computeServiceName(id), uri);
	}

    @Override
    public String[] getEnvPrefixes() {
        return new String[]{ "REDISTOGO_URL", "REDISCLOUD_URL", "OPENREDIS_URL", "REDISGREEN_URL" };
    }
}
