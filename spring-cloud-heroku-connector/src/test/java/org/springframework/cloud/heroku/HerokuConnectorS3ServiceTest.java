package org.springframework.cloud.heroku;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.S3ServiceInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Arthur Halet
 */
public class HerokuConnectorS3ServiceTest extends AbstractHerokuConnectorTest {

    private final static String bucketName = "mybucket";

    @Test
    public void s3ServiceCreationFromEnvVar() {
        Map<String, String> env = new HashMap<String, String>();
        String s3Url = getS3ServiceUrl();
        env.put("S3_URL", s3Url);
        when(mockEnvironment.getEnv()).thenReturn(env);
        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        ServiceInfo serviceInfo = getServiceInfo(serviceInfos, "S3");
        assertNotNull(serviceInfo);
        assertTrue(serviceInfo instanceof S3ServiceInfo);
        assertS3ServiceInfo((S3ServiceInfo) serviceInfo, hostname, port);
    }

    @Test
    public void s3ServiceCreationFromDetectableService() {
        for (S3DetectableService s3DetectableService : S3DetectableService.values()) {
            when(mockEnvironment.getEnv()).thenReturn(this.getEnvDetectableService(s3DetectableService));
            List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
            ServiceInfo serviceInfo = getServiceInfo(serviceInfos, bucketName);
            assertNotNull(serviceInfo);
            assertTrue(serviceInfo instanceof S3ServiceInfo);
            assertS3ServiceInfo((S3ServiceInfo) serviceInfo, this.getHost(s3DetectableService, bucketName), -1);
        }

    }

    private String getHost(S3DetectableService s3DetectableService, String bucketName) {
        URI uri = URI.create(s3DetectableService.getBaseUrl().replace("$bucketName", bucketName));
        return uri.getHost();
    }

    private Map<String, String> getEnvDetectableService(S3DetectableService s3DetectableService) {
        Map<String, String> env = new HashMap<String, String>();
        env.put(s3DetectableService.getBucketNameEnvKey(), bucketName);
        env.put(s3DetectableService.getAccessKeyIdEnvKey(), username);
        env.put(s3DetectableService.getSecretAccessKeyEnvKey(), password);
        return env;
    }

    private String getS3ServiceUrl() {
        String template = "s3://$username:$password@$hostname:$port/$bucket";

        return template.replace("$username", username).
                replace("$password", password).
                replace("$hostname", hostname).
                replace("$port", Integer.toString(port)).
                replace("$bucket", bucketName);
    }

    protected void assertS3ServiceInfo(S3ServiceInfo serviceInfo, String host, int port) {
        assertEquals(host, serviceInfo.getHost());
        assertEquals(port, serviceInfo.getPort());
        assertEquals(username, serviceInfo.getUserName());
        assertEquals(password, serviceInfo.getPassword());
        assertEquals(bucketName, serviceInfo.getBucket());
    }
}
