package org.springframework.cloud.heroku;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorPostgresqlServiceTest extends AbstractHerokuConnectorRelationalServiceTest {
    public HerokuConnectorPostgresqlServiceTest() {
        super("postgres");
    }
    
    @Test
    public void postgresqlServiceCreation() {
        Map<String, String> env = new HashMap<String, String>();
        String postgresUrl = getRelationalServiceUrl("db");
        env.put("HEROKU_POSTGRESQL_YELLOW_URL", postgresUrl);
        when(mockEnvironment.getEnv()).thenReturn(env);

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        ServiceInfo serviceInfo = getServiceInfo(serviceInfos, "HEROKU_POSTGRESQL_YELLOW_URL");
        assertNotNull(serviceInfo);
        assertTrue(serviceInfo instanceof PostgresqlServiceInfo);
        assertReleationServiceInfo((PostgresqlServiceInfo)serviceInfo, "db");
    }
}
