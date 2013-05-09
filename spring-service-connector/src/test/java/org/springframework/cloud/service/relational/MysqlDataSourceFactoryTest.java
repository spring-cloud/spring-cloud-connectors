package org.springframework.cloud.service.relational;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlDataSourceFactoryTest extends AbstractDataSourceFactoryTest<MysqlServiceInfo> {
	public MysqlServiceInfo getTestServiceInfo(String id) {
		return new MysqlServiceInfo(id, "host", 0, "database", "userName", "password");
	}
}
