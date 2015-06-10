package org.springframework.cloud.cloudfoundry;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.CassandraClusterServiceInfo;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Vinicius Carvalho
 */
public class CloudFoundryConnectorCassandraServiceTest extends AbstractCloudFoundryConnectorTest {

    @Test
    public void cloudfoundryConnection() throws Exception {
        when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
                readTestDataFile("test-cassandra-info.json")
        );
        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        assertServiceFoundOfType(serviceInfos, "time-series-db", CassandraClusterServiceInfo.class);
        CassandraClusterServiceInfo serviceInfo = (CassandraClusterServiceInfo)serviceInfos.get(0);
        Assert.assertEquals(2,serviceInfo.getNodes().size());

    }

}
