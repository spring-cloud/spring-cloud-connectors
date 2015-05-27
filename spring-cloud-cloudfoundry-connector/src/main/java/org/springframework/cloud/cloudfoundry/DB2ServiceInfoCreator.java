package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.DB2ServiceInfo;

public class DB2ServiceInfoCreator extends RelationalServiceInfoCreator<DB2ServiceInfo> {
	public DB2ServiceInfoCreator() {
		super(new Tags("sqldb","dashDB","db2"), DB2ServiceInfo.DB2_SCHEME);
	}

	@Override
	public DB2ServiceInfo createServiceInfo(String id, String url) {
		return new DB2ServiceInfo(id, url);
	}
}
