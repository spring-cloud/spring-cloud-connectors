package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudDataSourceFactoryParserPostgresqlTest extends CloudDataSourceFactoryParserTest {

	@Override
	protected BaseServiceInfo createService(String id) {
		return createPostgresqlService(id);
	}

}
