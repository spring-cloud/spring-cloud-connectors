package org.springframework.cloud.pcf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.pcf.gemfire.GemfireXDServiceInfo;
import org.springframework.cloud.pcf.hadoop.HadoopServiceInfo;
import org.springframework.cloud.service.BaseCompositeServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PhdServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {
    @Test
    public void phdServiceCreation() {
        String hadoopUsername = "hadoop-user";
        String hadoopHost = "hadoop-host";
        int hadoopPort = 6000;
        String hadoopDirectory = "hadoop-dir";
        String yarnResourceHost = "yark-resource-host";
        int yarnResourcePort = 7000;
        String yarnResourceSchedulerHost = "yarn-resource-scheduler-host";
        int yarnResourceSchedulerPort = 8000;
        String yarnMapReduceDir = "yarn-map-reduce-dir";
        String yarnStagingDir = "yarn-staging-dir";
        String hawkHost = "hawk-host";
        int hawkPort = 9000;
        String hawkUsername = "hawk-user";
        String hawkPassword = "hawk-pass";
        String gemHost = "gem-host";
        int gemPort = 10000;
        String gemUsername = "gem-user";
        String gemPassword = "gem-pass";
        String gemWorkingDir = "gem-working-dir";
        
        when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
                getServicesPayload(
                        getPhdServicePayload("phd-1", hadoopUsername, hadoopHost, hadoopPort, hadoopDirectory, 
                                yarnResourceHost, yarnResourcePort, 
                                yarnResourceSchedulerHost, yarnResourceSchedulerPort, yarnMapReduceDir, yarnStagingDir, 
                                hawkHost, hawkPort, hawkUsername, hawkPassword, 
                                gemHost, gemPort, gemUsername, gemPassword, gemWorkingDir)));

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        BaseCompositeServiceInfo phdServiceInfo = (BaseCompositeServiceInfo) getServiceInfo(serviceInfos, "phd-1");
        assertNotNull(phdServiceInfo);
        assertEquals(3, phdServiceInfo.getServiceInfos().size());
        
        HadoopServiceInfo hadoopServiceInfo = extractServiceInfo(phdServiceInfo.getServiceInfos(), HadoopServiceInfo.class);
        assertNotNull(hadoopServiceInfo);
        assertHadoopServiceInfo("phd-1/hadoop", hadoopHost, hadoopPort, 
                                yarnResourceHost, yarnResourcePort, yarnResourceSchedulerHost, yarnResourceSchedulerPort, hadoopServiceInfo);
        
        PostgresqlServiceInfo hawqServiceInfo = extractServiceInfo(phdServiceInfo.getServiceInfos(), PostgresqlServiceInfo.class);
        assertEquals("phd-1/hawq", hawqServiceInfo.getId());
        assertNotNull(hawqServiceInfo);

        GemfireXDServiceInfo gemfireXDServiceInfo = extractServiceInfo(phdServiceInfo.getServiceInfos(), GemfireXDServiceInfo.class);
        assertNotNull(gemfireXDServiceInfo);
        assertEquals("phd-1/gemfirexd", gemfireXDServiceInfo.getId());
    }
    
    private void assertHadoopServiceInfo(String serviceId, String hadoopHost, int hadoopPort, 
                                         String yarnResourceHost, int yarnResourcePort, 
                                         String yarnResourceSchedulerHost, int yarnResourceSchedulerPort,
                                         HadoopServiceInfo serviceInfo) {
        assertEquals(serviceId, serviceInfo.getId());
        assertEquals(String.format("hdfs://%s:%s", hadoopHost, hadoopPort), serviceInfo.getDefaultFS());
        assertEquals(String.format("%s:%s", yarnResourceHost, yarnResourcePort), serviceInfo.getYarnResourceManagerAddress());
        assertEquals(String.format("%s:%s", yarnResourceSchedulerHost, yarnResourceSchedulerPort), serviceInfo.getYarnResourceManagerSchedulerAddress());
    }
    
    @SuppressWarnings("unchecked")
    private <T> T extractServiceInfo(List<ServiceInfo> serviceInfos, Class<T> typeToSearch) {
        for (ServiceInfo serviceInfo: serviceInfos) {
            if (serviceInfo.getClass().equals(typeToSearch)) {
                return (T)serviceInfo;
            }
        }
        return null;
    }

    private String getPhdServicePayload(String serviceName, 
                                        String hadoopUsername, String hadoopHost, int hadoopPort, String hadoopDirectory,
                                        String yarnResourceHost, int yarnResourcePort, 
                                        String yarnResourceSchedulerHost, int yarnResourceSchedulerPort,
                                        String yarnMapReduceDir, String yarnStagingDir,
                                        String hawkHost, int hawkPort, String hawkUsername, String hawkPassword,
                                        String gemHost, int gemPort, String gemUsername, String gemPassword, String gemWorkingDir) {
        return getPhdServicePayload("test-phd-info.json", serviceName, hadoopUsername, hadoopHost, hadoopPort, hadoopDirectory,
                                    yarnResourceHost, yarnResourcePort, 
                                    yarnResourceSchedulerHost, yarnResourceSchedulerPort,
                                    yarnMapReduceDir, yarnStagingDir,
                                    hawkHost, hawkPort, hawkUsername, hawkPassword,
                                    gemHost, gemPort, gemUsername, gemPassword, gemWorkingDir);
    }

    private String getPhdServicePayload(String filename, String serviceName, 
                                        String hadoopUsername, String hadoopHost, int hadoopPort, String hadoopDirectory,
                                        String yarnResourceHost, int yarnResourcePort, 
                                        String yarnResourceSchedulerHost, int yarnResourceSchedulerPort,
                                        String yarnMapReduceDir, String yarnStagingDir,
                                        String hawkHost, int hawkPort, String hawkUsername, String hawkPassword,
                                        String gemHost, int gemPort, String gemUsername, String gemPassword, String gemWorkingDir) {
        String payload = readTestDataFile(filename);
        payload = payload.replace("$serviceName", serviceName);
        
        payload = payload.replace("$hadoop-username", hadoopUsername);
        payload = payload.replace("$hdfs-host", hadoopHost);
        payload = payload.replace("$hdfs-port", Integer.toString(hadoopPort));
        payload = payload.replace("$hdfs-directory", hadoopDirectory);

        payload = payload.replace("$yarn-resource-host", yarnResourceHost);
        payload = payload.replace("$yarn-resource-port", Integer.toString(yarnResourcePort));
        payload = payload.replace("$yarn-resource-scheduler-host", yarnResourceSchedulerHost);
        payload = payload.replace("$yarn-resource-scheduler-port", Integer.toString(yarnResourceSchedulerPort));
        payload = payload.replace("$yarn-map-reduce-dir", yarnMapReduceDir);
        payload = payload.replace("$yarn-staging-dir", yarnStagingDir);
        
        payload = payload.replace("$hawk-host", hawkHost);
        payload = payload.replace("$hawk-port", Integer.toString(hawkPort));
        payload = payload.replace("$hawk-username", hawkUsername);
        payload = payload.replace("$hawk-password", hawkPassword);

        payload = payload.replace("$gem-host", gemHost);
        payload = payload.replace("$gem-port", Integer.toString(gemPort));
        payload = payload.replace("$gem-username", gemUsername);
        payload = payload.replace("$gem-password", gemPassword);
        payload = payload.replace("$gem-working-dir", gemWorkingDir);
        
        return payload;
    }
}
