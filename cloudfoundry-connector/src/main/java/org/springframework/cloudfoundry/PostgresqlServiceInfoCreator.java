package org.springframework.cloudfoundry;

import org.springframework.cloud.service.relational.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlServiceInfoCreator extends RelationalServiceInfoCreator<PostgresqlServiceInfo> {

	public PostgresqlServiceInfoCreator() {
		super("postgresql");
	}

	@Override
	public PostgresqlServiceInfo createServiceInfo(String id, String host, int port,
			String database, String userName, String password) {
		return new PostgresqlServiceInfo(id, host, port, database, userName, password);
	}
}
