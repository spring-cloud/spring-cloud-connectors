package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.DB2ServiceInfo;;

public class DB2DataSourceCreator extends DataSourceCreator<DB2ServiceInfo> {

	private static final String[] DRIVERS = new String[]{"com.ibm.db2.jcc.DB2Driver"};
	private static final String VALIDATION_QUERY = "select 1 from sysibm.sysdummy1";

	public DB2DataSourceCreator() {
		super("spring-cloud.db2.driver", DRIVERS, VALIDATION_QUERY);
	}
}
