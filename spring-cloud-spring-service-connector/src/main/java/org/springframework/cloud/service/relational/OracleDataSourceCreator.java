package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.OracleServiceInfo;

public class OracleDataSourceCreator extends DataSourceCreator<OracleServiceInfo> {

	public static final String[] DRIVERS = new String[]{"oracle.jdbc.OracleDriver"};
	public static final String VALIDATION_QUERY = "SELECT 'Y' from dual";

	public OracleDataSourceCreator() {
		super("spring-cloud.oracle.driver", DRIVERS, VALIDATION_QUERY);
	}
}
