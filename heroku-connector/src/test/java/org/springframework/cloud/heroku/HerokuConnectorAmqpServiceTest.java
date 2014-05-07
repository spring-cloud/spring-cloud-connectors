package org.springframework.cloud.heroku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorAmqpServiceTest extends AbstractHerokuConnectorTest {

    
    @Test
    public void amqpServiceCreation() {
        Map<String, String> env = new HashMap<String, String>();
        String amqpUrl = getAmqpServiceUrl("db");
        env.put("CLOUDAMQP_URL", amqpUrl);
        when(mockEnvironment.getEnv()).thenReturn(env);

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        ServiceInfo serviceInfo = getServiceInfo(serviceInfos, "CLOUDAMQP");
        assertNotNull(serviceInfo);
        assertTrue(serviceInfo instanceof AmqpServiceInfo);
        assertAmqpServiceInfo((AmqpServiceInfo)serviceInfo, "db");
    }
    
    private String getAmqpServiceUrl(String name) {
        String template = "amqp://$username:$password@$hostname:$port/$virtualHost";

        return template.replace("$username", username).
                        replace("$password", password).
                        replace("$hostname", hostname).
                        replace("$port", Integer.toString(port)).
                        replace("$virtualHost", name);
    }   
    
    protected void assertAmqpServiceInfo(AmqpServiceInfo serviceInfo, String virtualHost) {
        assertEquals(hostname, serviceInfo.getHost());
        assertEquals(port, serviceInfo.getPort());
        assertEquals(username, serviceInfo.getUserName());
        assertEquals(password, serviceInfo.getPassword());
        assertEquals(virtualHost, serviceInfo.getPath());
    }
    
}
