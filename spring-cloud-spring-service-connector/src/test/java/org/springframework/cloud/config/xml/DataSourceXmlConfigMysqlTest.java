package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.relational.MysqlDataSourceCreator;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceXmlConfigMysqlTest extends DataSourceXmlConfigTest {
	protected ServiceInfo createService(String id) {
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
