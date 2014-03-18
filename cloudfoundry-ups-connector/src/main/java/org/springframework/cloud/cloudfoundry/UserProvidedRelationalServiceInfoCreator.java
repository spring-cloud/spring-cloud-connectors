package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.RelationalServiceInfo;

import java.util.Map;

public abstract class UserProvidedRelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends RelationalServiceInfoCreator<SI> {
	private final String uriScheme;

	public UserProvidedRelationalServiceInfoCreator(String uriScheme) {
		super("user-provided");
		this.uriScheme = uriScheme;
	}

	public abstract SI createServiceInfo(String id, String uri);

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
