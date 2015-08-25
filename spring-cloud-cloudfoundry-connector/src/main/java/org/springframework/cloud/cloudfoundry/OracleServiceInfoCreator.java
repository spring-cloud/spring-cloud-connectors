package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.OracleServiceInfo;

public class OracleServiceInfoCreator extends RelationalServiceInfoCreator<OracleServiceInfo> {
	public OracleServiceInfoCreator() {
		super(new Tags(), OracleServiceInfo.ORACLE_SCHEME);
	}

	@Override
	public OracleServiceInfo createServiceInfo(String id, String url, String jdbcUrl) {
		return new OracleServiceInfo(id, url, jdbcUrl);
	}
}
