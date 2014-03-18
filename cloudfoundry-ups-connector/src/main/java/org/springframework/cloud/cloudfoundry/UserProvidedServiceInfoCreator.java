package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.ServiceInfo;

import java.util.Map;

public abstract class UserProvidedServiceInfoCreator<SI extends ServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {
	private final String uriScheme;

	public UserProvidedServiceInfoCreator(String uriScheme) {
		super("user-provided");
		this.uriScheme = uriScheme;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean accept(Map<String, Object> serviceData) {
		if (super.accept(serviceData)) {
			Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
			String uri = (String) credentials.get("uri");

			if (uri != null && uri.startsWith(uriScheme)) {
				return true;
			}
		}

		return false;
	}
}
