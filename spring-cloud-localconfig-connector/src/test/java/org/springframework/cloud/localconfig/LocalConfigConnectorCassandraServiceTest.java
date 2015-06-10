package org.springframework.cloud.localconfig;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.CassandraClusterServiceInfo;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author  Vinicius Carvalho
 */
public class LocalConfigConnectorCassandraServiceTest extends AbstractLocalConfigConnectorWithUrisTest{

    @Test
    public void serviceCreation() throws Exception{
        List<ServiceInfo> services = connector.getServiceInfos();
        ServiceInfo service = getServiceInfo(services, "cassandra");
        assertNotNull(service);
        assertTrue(service instanceof CassandraClusterServiceInfo);
        assertUriParameters((CassandraClusterServiceInfo) service);

        assertTrue(((CassandraClusterServiceInfo)service).getNodes().size() == 1);
    }

}
