package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 *
 * @author Christopher Smith
 *
 */
public class PostgresqlServiceInfoCreator extends LocalConfigServiceInfoCreator<PostgresqlServiceInfo>{

    public PostgresqlServiceInfoCreator() {
        super(PostgresqlServiceInfo.POSTGRES_SCHEME);
    }

    @Override
    public PostgresqlServiceInfo createServiceInfo(String id, String uri) {
        return new PostgresqlServiceInfo(id, uri);
    }
}
