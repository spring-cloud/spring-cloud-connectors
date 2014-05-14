package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceXmlConfigPostgesqlTest extends DataSourceXmlConfigTest {

	@Override
	protected ServiceInfo createService(String id) {
		return createPostgresqlService(id);
	}

}
