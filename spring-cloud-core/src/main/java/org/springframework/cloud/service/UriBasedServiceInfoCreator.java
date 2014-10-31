package org.springframework.cloud.service;

import org.springframework.cloud.ServiceInfoCreator;

public abstract class UriBasedServiceInfoCreator<SI extends ServiceInfo>
		implements ServiceInfoCreator<ServiceInfo, UriBasedServiceData> {

	private final String[] uriSchemes;

	public UriBasedServiceInfoCreator(String... uriSchemes) {
		this.uriSchemes = uriSchemes;
	}

	@Override
	public boolean accept(UriBasedServiceData serviceData) {
		String uriString = serviceData.getUri();
		for (String uriScheme : uriSchemes) {
			if (uriString.startsWith(uriScheme + "://")) {
				return true;
			}
		}
		return false;
	}

	public abstract SI createServiceInfo(String id, String uri);

	@Override
	public SI createServiceInfo(UriBasedServiceData serviceData) {
		return createServiceInfo(serviceData.getKey(), serviceData.getUri());
	}
}
