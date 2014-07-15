package org.springframework.cloud.service;

import org.springframework.cloud.ServiceInfoCreator;

public abstract class UriBasedServiceInfoCreator<SI extends ServiceInfo> implements
    ServiceInfoCreator<ServiceInfo, UriBasedServiceData> {

    private final String uriScheme;

    public UriBasedServiceInfoCreator(String uriScheme) {
        this.uriScheme = uriScheme;
    }

    @Override
    public boolean accept(UriBasedServiceData serviceData) {
        return serviceData.getUri().toString().startsWith(uriScheme + "://");
    }

    public abstract SI createServiceInfo(String id, String uri);

    @Override
    public SI createServiceInfo(UriBasedServiceData serviceData) {
        return createServiceInfo(serviceData.getKey(), serviceData.getUri());
    }
}
