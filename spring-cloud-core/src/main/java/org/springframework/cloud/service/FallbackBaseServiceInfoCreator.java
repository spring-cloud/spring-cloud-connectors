package org.springframework.cloud.service;

import org.springframework.cloud.FallbackServiceInfoCreator;
import org.springframework.cloud.AbstractCloudConnector.KeyValuePair;

public class FallbackBaseServiceInfoCreator extends FallbackServiceInfoCreator<BaseServiceInfo, KeyValuePair> {
    @Override
    public BaseServiceInfo createServiceInfo(KeyValuePair serviceData) {
        return new BaseServiceInfo(serviceData.getKey());
    }
}
