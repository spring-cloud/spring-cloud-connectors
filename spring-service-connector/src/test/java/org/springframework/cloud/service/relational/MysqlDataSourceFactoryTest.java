package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.MysqlServiceInfo;

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
