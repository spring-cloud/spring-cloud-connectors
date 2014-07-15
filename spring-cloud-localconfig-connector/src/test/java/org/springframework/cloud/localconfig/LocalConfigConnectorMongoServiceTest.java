package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MongoServiceInfo;

public class LocalConfigConnectorMongoServiceTest extends AbstractLocalConfigConnectorTest {

    @Test
    public void serviceCreation() {
        List<ServiceInfo> services = connector.getServiceInfos();
        ServiceInfo service = getServiceInfo(services, "candygram");
        assertNotNull(service);
        assertTrue(service instanceof MongoServiceInfo);
        assertUriParameters((MongoServiceInfo) service);
    }

}
