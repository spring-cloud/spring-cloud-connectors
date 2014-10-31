package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.RedisServiceInfo;

/**
 *
 * @author Christopher Smith
 *
 */
public class RedisServiceInfoCreator extends LocalConfigServiceInfoCreator<RedisServiceInfo>{

    public RedisServiceInfoCreator() {
        super(RedisServiceInfo.REDIS_SCHEME);
    }

    @Override
    public RedisServiceInfo createServiceInfo(String id, String uri) {
        return new RedisServiceInfo(id, uri);
    }
}
