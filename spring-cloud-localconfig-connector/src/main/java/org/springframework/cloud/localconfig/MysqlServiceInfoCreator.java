package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.MysqlServiceInfo;

/**
 * @author Christopher Smith
 */
public class MysqlServiceInfoCreator extends LocalConfigServiceInfoCreator<MysqlServiceInfo> {

	public MysqlServiceInfoCreator() {
		super(MysqlServiceInfo.MYSQL_SCHEME);
	}

	@Override
	public MysqlServiceInfo createServiceInfo(String id, String uri) {
		return new MysqlServiceInfo(id, uri);
	}
}
