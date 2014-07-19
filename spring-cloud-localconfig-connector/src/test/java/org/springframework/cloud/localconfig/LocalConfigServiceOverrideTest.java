package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MongoServiceInfo;

public class LocalConfigServiceOverrideTest extends AbstractLocalConfigConnectorTest {

    private PassthroughEnvironmentAccessor env;

    @Before
    public void injectEnvironment() {
        env = new PassthroughEnvironmentAccessor();
        connector.setEnvironmentAccessor(env);
    }

    @Test
    public void serviceOverride() {
        env.setSystemProperty("spring.cloud.candygram", "mongodb://youruser:yourpass@40.30.20.10:4321/dbname");

        List<ServiceInfo> services = connector.getServiceInfos();
        ServiceInfo service = getServiceInfo(services, "candygram");
        assertNotNull(service);
        assertTrue(service instanceof MongoServiceInfo);
        MongoServiceInfo mongo = (MongoServiceInfo) service;
        assertEquals("youruser", mongo.getUserName());
        assertEquals(4321, mongo.getPort());
    }
}
