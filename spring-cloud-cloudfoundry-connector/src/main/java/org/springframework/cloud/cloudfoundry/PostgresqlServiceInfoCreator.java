package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

import static org.springframework.cloud.service.common.PostgresqlServiceInfo.POSTGRES_JDBC_SCHEME;
import static org.springframework.cloud.service.common.PostgresqlServiceInfo.POSTGRES_SCHEME;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlServiceInfoCreator extends RelationalServiceInfoCreator<PostgresqlServiceInfo> {

	public PostgresqlServiceInfoCreator() {
		super(new Tags("postgresql"), POSTGRES_SCHEME, POSTGRES_JDBC_SCHEME);
	}

	@Override
	public PostgresqlServiceInfo createServiceInfo(String id, String url, String jdbcUrl) {
		return new PostgresqlServiceInfo(id, url, jdbcUrl);
	}
}
