package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.OracleServiceInfo;

/**
 * Oracle ServiceInfo creator for localconfig connector. 
 * @author Jason Woodrich
 */
public class OracleServiceInfoCreator extends LocalConfigServiceInfoCreator<OracleServiceInfo> {
	public OracleServiceInfoCreator() {
		super(OracleServiceInfo.ORACLE_SCHEME);
	}
	@Override
	public OracleServiceInfo createServiceInfo(String id, String uri) {
		return new OracleServiceInfo(id,uri);
	}

}
