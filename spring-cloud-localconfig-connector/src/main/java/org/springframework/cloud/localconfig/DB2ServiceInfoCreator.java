package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.DB2ServiceInfo;

/**
 * DB2 ServiceInfoCreator for the localconfig connector.
 *
 * @author Scott Frederick
 */
public class DB2ServiceInfoCreator extends LocalConfigServiceInfoCreator<DB2ServiceInfo> {

	public DB2ServiceInfoCreator() {
		super(DB2ServiceInfo.DB2_SCHEME);
	}

	@Override
	public DB2ServiceInfo createServiceInfo(String id, String uri) {
		return new DB2ServiceInfo(id, uri);
	}
}
