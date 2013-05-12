package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.MysqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlServiceInfoCreator extends RelationalServiceInfoCreator<MysqlServiceInfo> {

	public MysqlServiceInfoCreator() {
		super("mysql");
	}

	@Override
	public MysqlServiceInfo createServiceInfo(String id, String host, int port,
			String database, String userName, String password) {
		return new MysqlServiceInfo(id, host, port, database, userName, password);
	}
}
