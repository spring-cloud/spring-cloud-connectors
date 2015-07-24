package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.SqlServerServiceInfo;

public class SqlServerServiceInfoCreator extends
		RelationalServiceInfoCreator<SqlServerServiceInfo> {

	public SqlServerServiceInfoCreator() {
		super(new Tags(), SqlServerServiceInfo.SQLSERVER_SCHEME);	}

	@Override
	public SqlServerServiceInfo createServiceInfo(String id, String url) {
		return new SqlServerServiceInfo(id, url);
	}

}
