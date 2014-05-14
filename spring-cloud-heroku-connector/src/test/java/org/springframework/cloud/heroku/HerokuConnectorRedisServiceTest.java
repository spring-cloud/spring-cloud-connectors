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
import org.springframework.cloud.service.common.RedisServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorRedisServiceTest extends AbstractHerokuConnectorTest {

    @Test
    public void redisServiceCreation() {
        for (String redisEnv: new String[]{"REDISTOGO_URL", "REDISCLOUD_URL", "OPENREDIS_URL", "REDISGREEN_URL"}) {
            Map<String, String> env = new HashMap<String, String>();
            String redisUrl = getRedisServiceUrl();
            env.put(redisEnv, redisUrl);
            when(mockEnvironment.getEnv()).thenReturn(env);
    
            List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
            ServiceInfo serviceInfo = getServiceInfo(serviceInfos, redisEnv.substring(0, redisEnv.length()-4));
            assertNotNull(serviceInfo);
            assertTrue(serviceInfo instanceof RedisServiceInfo);
            assertRedisServiceInfo((RedisServiceInfo)serviceInfo);
        }
    }
    
    private String getRedisServiceUrl() {
        String template = "redis://$username:$password@$hostname:$port";

        return template.replace("$username", username).
                        replace("$password", password).
                        replace("$hostname", hostname).
                        replace("$port", Integer.toString(port));
    }   
    
    protected void assertRedisServiceInfo(RedisServiceInfo serviceInfo) {
        assertEquals(hostname, serviceInfo.getHost());
        assertEquals(port, serviceInfo.getPort());
        assertEquals(username, serviceInfo.getUserName());
        assertEquals(password, serviceInfo.getPassword());
    }
    
}
