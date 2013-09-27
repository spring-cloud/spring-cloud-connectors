package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceXmlConfigMysqlTest extends DataSourceXmlConfigTest {

	protected ServiceInfo createService(String id) {
		return createMysqlService(id);
	}


}
