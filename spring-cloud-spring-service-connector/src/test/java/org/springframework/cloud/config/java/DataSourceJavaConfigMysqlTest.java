package org.springframework.cloud.config.java;

import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.relational.MysqlDataSourceCreator;

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

	@Override
	protected String getDriverClassName() {
		return MysqlDataSourceCreator.DRIVERS[0];
	}

	@Override
	protected String getValidationQuery() {
		return MysqlDataSourceCreator.VALIDATION_QUERY;
	}
}


