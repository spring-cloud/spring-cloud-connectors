package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("postgresql")
public class PostgresqlServiceInfo extends RelationalServiceInfo {
	public PostgresqlServiceInfo(String id, String host, int port, String database,
			String userName, String password) {
		super(id, host, port, database, userName, password);
	}

	
	@Override
	public String getUrl() {
		return "jdbc:postgresql://" + getHost() + ":" + + getPort() + "/" + database;
	}
}
