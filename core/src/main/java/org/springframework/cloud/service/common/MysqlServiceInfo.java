package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("mysql")
public class MysqlServiceInfo extends RelationalServiceInfo {

	public MysqlServiceInfo(String id, String host, int port, String database,
			String userName, String password) {
		super(id, host, port, database, userName, password);
	}

	public String getUrl() {
		return "jdbc:mysql://" + getHost() + ":" + + getPort() + "/" + database;
	}

}
