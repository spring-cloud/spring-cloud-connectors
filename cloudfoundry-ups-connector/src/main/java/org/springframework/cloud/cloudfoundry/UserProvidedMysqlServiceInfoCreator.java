package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.MysqlServiceInfo;

public class UserProvidedMysqlServiceInfoCreator extends UserProvidedRelationalServiceInfoCreator<MysqlServiceInfo> {
	public UserProvidedMysqlServiceInfoCreator() {
		super("mysql:");
	}

	@Override
	public MysqlServiceInfo createServiceInfo(String id, String url) {
		return new MysqlServiceInfo(id, url);
	}
}

