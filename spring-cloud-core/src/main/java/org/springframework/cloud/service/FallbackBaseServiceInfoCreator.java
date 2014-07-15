package org.springframework.cloud.service;

import org.springframework.cloud.FallbackServiceInfoCreator;

public class FallbackBaseServiceInfoCreator extends FallbackServiceInfoCreator<BaseServiceInfo, UriBasedServiceData> {
    @Override
    public BaseServiceInfo createServiceInfo(UriBasedServiceData serviceData) {
        return new BaseServiceInfo(serviceData.getKey());
    }
}
