package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;

public class LocalConfigConnectorMysqlServiceTest extends AbstractLocalConfigConnectorWithUrisTest {

    @Test
    public void serviceCreation() {
        List<ServiceInfo> services = connector.getServiceInfos();
        ServiceInfo service = getServiceInfo(services, "maria");
        assertNotNull(service);
        assertTrue(service instanceof MysqlServiceInfo);
        assertUriParameters((MysqlServiceInfo) service);
    }

}
