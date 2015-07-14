package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.SqlServerServiceInfo;
import org.springframework.cloud.service.relational.DataSourceCreator;

public class SqlServerDataSourceCreator extends DataSourceCreator<SqlServerServiceInfo> {

	private static final String[] DRIVERS = new String[]{"com.microsoft.sqlserver.jdbc.SQLServerDriver"};
	private static final String VALIDATION_QUERY = "SELECT 1";

	public SqlServerDataSourceCreator() {
		super("spring-cloud.sqlserver.driver", DRIVERS, VALIDATION_QUERY);
	}
	
}
