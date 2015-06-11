package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.DB2ServiceInfo;

public class DB2DataSourceFactoryTest extends AbstractDataSourceFactoryTest<DB2ServiceInfo> {
	public DB2ServiceInfo getTestServiceInfo(String id) {
		return new DB2ServiceInfo(id, "db2://host:port/db:user=myuser;password=mypassword;");
	}
}
