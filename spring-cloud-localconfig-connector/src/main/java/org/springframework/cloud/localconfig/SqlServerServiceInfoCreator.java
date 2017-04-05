package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.SqlServerServiceInfo;

/**
 * DB2 ServiceInfoCreator for the localconfig connector.
 *
 * @author Scott Frederick
 */
public class SqlServerServiceInfoCreator extends LocalConfigServiceInfoCreator<SqlServerServiceInfo> {

	public SqlServerServiceInfoCreator() {
		super(SqlServerServiceInfo.SQLSERVER_SCHEME);
	}

	@Override
	public SqlServerServiceInfo createServiceInfo(String id, String uri) {
		return new SqlServerServiceInfo(id, uri);
	}
}
