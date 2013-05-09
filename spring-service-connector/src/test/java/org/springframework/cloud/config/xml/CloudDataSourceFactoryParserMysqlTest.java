package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudDataSourceFactoryParserMysqlTest extends CloudDataSourceFactoryParserTest {

	protected BaseServiceInfo createService(String id) {
		return createMysqlService(id);
	}


}
