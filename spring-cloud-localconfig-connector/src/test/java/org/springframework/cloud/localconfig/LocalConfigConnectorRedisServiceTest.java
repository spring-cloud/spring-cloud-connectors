package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.RedisServiceInfo;

public class LocalConfigConnectorRedisServiceTest extends AbstractLocalConfigConnectorWithUrisTest {

    @Test
    public void serviceCreation() {
        List<ServiceInfo> services = connector.getServiceInfos();
        ServiceInfo service = getServiceInfo(services, "blue");
        assertNotNull(service);
        assertTrue(service instanceof RedisServiceInfo);
        assertUriParameters((RedisServiceInfo) service);
    }

}
