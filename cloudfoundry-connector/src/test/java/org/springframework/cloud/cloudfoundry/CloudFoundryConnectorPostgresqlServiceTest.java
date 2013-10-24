package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorPostgresqlServiceTest extends AbstractCloudFoundryConnectorRelationalServiceTest {
    @Test
    public void postgresqlServiceCreation() {
        String[] versions = {"9.1", "9.2"};
        String name1 = "database-1";
        String name2 = "database-2";
        for (String version : versions) {
            when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
                .thenReturn(getServicesPayload(
                                getPostgresqlServicePayload(version, "postgresql-1", hostname, port, username, password, name1),
                                getPostgresqlServicePayload(version, "postgresql-2", hostname, port, username, password, name2)));
        }

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        PostgresqlServiceInfo info1 = (PostgresqlServiceInfo) getServiceInfo(serviceInfos, "postgresql-1");
        PostgresqlServiceInfo info2 = (PostgresqlServiceInfo) getServiceInfo(serviceInfos, "postgresql-2");
        assertNotNull(info1);
        assertNotNull(info2);
        assertEquals(getJdbcUrl("postgres", name1), info1.getJdbcUrl());
        assertEquals(getJdbcUrl("postgres", name2), info2.getJdbcUrl());
    }

    private String getPostgresqlServicePayload(String version, String serviceName,
                                               String hostname, int port,
                                               String user, String password, String name) {
        return getRelationalPayload("test-postgresql-info.json", version, serviceName,
                hostname, port, user, password, name);
    }

    
}
