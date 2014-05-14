package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.OracleServiceInfo;

public class UserProvidedOracleServiceInfoCreator extends RelationalServiceInfoCreator<OracleServiceInfo> {
	public UserProvidedOracleServiceInfoCreator() {
		super(new Tags(), "oracle");
	}

	@Override
	public OracleServiceInfo createServiceInfo(String id, String url) {
		return new OracleServiceInfo(id, url);
	}
}
