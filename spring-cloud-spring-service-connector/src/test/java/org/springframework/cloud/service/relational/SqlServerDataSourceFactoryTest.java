package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.SqlServerServiceInfo;

public class SqlServerDataSourceFactoryTest extends AbstractDataSourceFactoryTest<SqlServerServiceInfo> {
	public SqlServerServiceInfo getTestServiceInfo(String id) {
		return new SqlServerServiceInfo(id, "sqlserver://username:pass@host:port/db");
	}
}
