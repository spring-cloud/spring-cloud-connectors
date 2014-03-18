package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

public class UserProvidedPostgresqlServiceInfoCreator extends UserProvidedRelationalServiceInfoCreator<PostgresqlServiceInfo> {
	public UserProvidedPostgresqlServiceInfoCreator() {
		super("postgres:");
	}

	@Override
	public PostgresqlServiceInfo createServiceInfo(String id, String url) {
		return new PostgresqlServiceInfo(id, url);
	}
}
