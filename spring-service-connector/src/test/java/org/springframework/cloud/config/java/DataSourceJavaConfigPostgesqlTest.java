package org.springframework.cloud.config.java;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceJavaConfigPostgesqlTest extends DataSourceJavaConfigTest {
	@Override
	public PostgresqlServiceInfo createService(String id) {
		return createPostgresqlService(id);
	}

}


