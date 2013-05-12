package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlDataSourceCreator extends DataSourceCreator<PostgresqlServiceInfo> {
	
	private static final String POSTGRESQL_DRIVER_CLASS_NAME = "org.postgresql.Driver";
	private static final String VALIDATION_QUERY = "SELECT 1";
	
	@Override
	public String getDriverClassName() {
		return POSTGRESQL_DRIVER_CLASS_NAME;
	}

	@Override
	public String getValidationQuery() {
		return VALIDATION_QUERY;
	}
}
