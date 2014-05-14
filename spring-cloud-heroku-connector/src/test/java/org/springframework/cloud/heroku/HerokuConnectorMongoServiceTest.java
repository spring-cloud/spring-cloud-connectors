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
import org.springframework.cloud.service.common.MongoServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnectorMongoServiceTest extends AbstractHerokuConnectorTest {

    
    @Test
    public void mongoServiceCreation() {
        for (String mongoEnv: new String[]{"MONGOLAB_URI", "MONGOHQ_URL", "MONGOSOUP_URL"}) {
            Map<String, String> env = new HashMap<String, String>();
            String mongoUrl = getMongoServiceUrl("db");
            env.put(mongoEnv, mongoUrl);
            when(mockEnvironment.getEnv()).thenReturn(env);
    
            List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
            ServiceInfo serviceInfo = getServiceInfo(serviceInfos, mongoEnv.substring(0, mongoEnv.length()-4));
            assertNotNull(serviceInfo);
            assertTrue(serviceInfo instanceof MongoServiceInfo);
            assertMongoServiceInfo((MongoServiceInfo)serviceInfo, "db");
        }
    }
    
    private String getMongoServiceUrl(String name) {
        String template = "mongodb://$username:$password@$hostname:$port/$db";

        return template.replace("$username", username).
                        replace("$password", password).
                        replace("$hostname", hostname).
                        replace("$port", Integer.toString(port)).
                        replace("$db", name);
    }   
    
    protected void assertMongoServiceInfo(MongoServiceInfo serviceInfo, String databaseName) {
        assertEquals(hostname, serviceInfo.getHost());
        assertEquals(port, serviceInfo.getPort());
        assertEquals(username, serviceInfo.getUserName());
        assertEquals(password, serviceInfo.getPassword());
        assertEquals(databaseName, serviceInfo.getPath());
    }
    
}
