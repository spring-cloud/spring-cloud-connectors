package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudDataSourceFactoryParserMysqlTest extends CloudDataSourceFactoryParserTest {

	protected ServiceInfo createService(String id) {
		return createMysqlService(id);
	}


}
