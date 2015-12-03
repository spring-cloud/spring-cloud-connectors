package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.relational.PostgresqlDataSourceCreator;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceXmlConfigPostgresqlTest extends DataSourceXmlConfigTest {
	@Override
	protected ServiceInfo createService(String id) {
		return createPostgresqlService(id);
	}

	@Override
	protected String getDriverClassName() {
		return PostgresqlDataSourceCreator.DRIVERS[0];
	}

	@Override
	protected String getValidationQuery() {
		return PostgresqlDataSourceCreator.VALIDATION_QUERY;
	}
}
