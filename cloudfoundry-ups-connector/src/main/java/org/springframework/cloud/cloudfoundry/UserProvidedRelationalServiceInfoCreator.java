package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.RelationalServiceInfo;

public abstract class UserProvidedRelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends RelationalServiceInfoCreator<SI> {
	public UserProvidedRelationalServiceInfoCreator(String uriScheme) {
		super("user-provided", uriScheme);
	}

	public abstract SI createServiceInfo(String id, String uri);
}
