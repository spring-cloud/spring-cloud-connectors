package org.springframework.cloud.service.relational;

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
