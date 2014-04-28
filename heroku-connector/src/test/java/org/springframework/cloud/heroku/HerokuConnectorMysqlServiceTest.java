package org.springframework.cloud.heroku;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorMysqlServiceTest extends AbstractHerokuConnectorRelationalServiceTest {
    public HerokuConnectorMysqlServiceTest() {
        super("mysql");
    }
    
    @Test
    public void mysqlServiceCreation() {
        Map<String, String> env = new HashMap<String, String>();
        String mysqlUrl = getRelationalServiceUrl("db");
        env.put("CLEARDB_DATABASE_URL", mysqlUrl);
        when(mockEnvironment.getEnv()).thenReturn(env);

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        ServiceInfo serviceInfo = getServiceInfo(serviceInfos, "mysql-service");
        assertNotNull(serviceInfo);
        assertTrue(serviceInfo instanceof MysqlServiceInfo);
        assertReleationServiceInfo((MysqlServiceInfo)serviceInfo, "db");
    }
}
