package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlDataSourceFactoryTest extends AbstractDataSourceFactoryTest<PostgresqlServiceInfo> {
	public PostgresqlServiceInfo getTestServiceInfo(String id) {
		return new PostgresqlServiceInfo(id, "host", 0, "database", "userName", "password");
	}
}
