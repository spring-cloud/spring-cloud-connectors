package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 *
 * @author Christopher Smith
 *
 */
public class AmqpServiceInfoCreator extends LocalConfigServiceInfoCreator<AmqpServiceInfo>{

    public AmqpServiceInfoCreator() {
        super(AmqpServiceInfo.URI_SCHEME);
    }

    @Override
    public AmqpServiceInfo createServiceInfo(String id, String uri) {
        return new AmqpServiceInfo(id, uri);
    }
}
