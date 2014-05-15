package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.OracleServiceInfo;

public class OracleServiceInfoCreator extends RelationalServiceInfoCreator<OracleServiceInfo> {
	public OracleServiceInfoCreator() {
		super(new Tags(), "oracle");
	}

	@Override
	public OracleServiceInfo createServiceInfo(String id, String url) {
		return new OracleServiceInfo(id, url);
	}
}
