package org.springframework.cloud.heroku;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlServiceInfoCreator extends RelationalServiceInfoCreator<PostgresqlServiceInfo> {

	public PostgresqlServiceInfoCreator() {
		super("postgres");
	}

	@Override
	public PostgresqlServiceInfo createServiceInfo(String id, String uri) {
		return new PostgresqlServiceInfo("postgres-service", uri);
	}
}
