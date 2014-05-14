package org.springframework.cloud.config.java;

import org.springframework.cloud.service.common.MysqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceJavaConfigMysqlTest extends DataSourceJavaConfigTest {
	@Override
	public MysqlServiceInfo createService(String id) {
		return createMysqlService(id);
	}

}


