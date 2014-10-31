package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlServiceInfoCreator extends RelationalServiceInfoCreator<PostgresqlServiceInfo> {

	public PostgresqlServiceInfoCreator() {
		super(new Tags("postgresql"), PostgresqlServiceInfo.POSTGRES_SCHEME);
	}

	@Override
	public PostgresqlServiceInfo createServiceInfo(String id, String url) {
		return new PostgresqlServiceInfo(id, url);
	}
}
