package org.springframework.cloud.service;

import org.springframework.cloud.KeyValuePair;
import org.springframework.cloud.ServiceInfoCreator;

public abstract class UriBasedServiceInfoCreator<SI extends ServiceInfo> implements
    ServiceInfoCreator<ServiceInfo, KeyValuePair> {

    private final String uriScheme;

    public UriBasedServiceInfoCreator(String uriScheme) {
        this.uriScheme = uriScheme;
    }

    @Override
    public boolean accept(KeyValuePair serviceData) {
        return serviceData.getValue().toString().startsWith(uriScheme + "://");
    }

    public abstract SI createServiceInfo(String id, String uri);

    @Override
    public SI createServiceInfo(KeyValuePair serviceData) {
        return createServiceInfo(serviceData.getKey(), serviceData.getValue());
    }
}
