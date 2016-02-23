package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.S3ServiceInfo;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p>
 * Author: Arthur Halet
 * Date: 16/09/2015
 */
public class CloudFoundryConnectorS3ServiceTest extends AbstractCloudFoundryConnectorTest {
    protected String accessKeyId = "mypublickey";
    protected String accessKeyIdUrlEncode = "mypublickey%3D%3D";
    protected String accessKeyIdUrlEncodeDecoded = "mypublickey==";
    protected String secretAccessKey = "mysecretkey";
    protected String secretAccessKeyUrlEncode = "mysecretkey%3D%3D";
    protected String secretAccessKeyUrlEncodeDecoded = "mysecretkey==";
    protected String bucketName1 = "bucket-1";
    protected String bucketName2 = "bucket-2";

    public CloudFoundryConnectorS3ServiceTest() {
    }

    protected static String getUrl() {

        return "https://" + "10.20.30.40" + ":" + 80;
    }

    @Test
    public void s3ServiceCreation() {
        when(this.mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(getServicesPayload(this.getS3ServicePayload("s3-1", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName1),
                this.getS3ServicePayload("s3-2", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName2)));
        List<ServiceInfo> serviceInfos = this.testCloudConnector.getServiceInfos();
        S3ServiceInfo info1 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-1");
        S3ServiceInfo info2 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-2");
        this.asserting(info1, info2);
        assertTrue("Info1 is an aws s3", !info1.isAwsS3());
        assertTrue("Info2 is an aws s3", !info2.isAwsS3());
        assertFalse("Info1 is an aws s3 with virtual host", info1.isVirtualHostBuckets());
        assertFalse("Info2 is an aws s3 with virtual host", info2.isVirtualHostBuckets());
        assertEquals(getUrl(), info1.getS3Host());
        assertEquals(getUrl(), info2.getS3Host());
    }

    @Test
    public void s3ServiceCreationUrlEncoded() {
        when(this.mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(getServicesPayload(this.getS3ServicePayload("s3-1", "10.20.30.40", 80, accessKeyIdUrlEncode, secretAccessKeyUrlEncode, bucketName1),
                this.getS3ServicePayload("s3-2", "10.20.30.40", 80, accessKeyIdUrlEncode, secretAccessKeyUrlEncode, bucketName2)));
        List<ServiceInfo> serviceInfos = this.testCloudConnector.getServiceInfos();
        S3ServiceInfo info1 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-1");
        S3ServiceInfo info2 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-2");
        this.assertingUrlEncode(info1, info2);
        assertTrue("Info1 is an aws s3", !info1.isAwsS3());
        assertTrue("Info2 is an aws s3", !info2.isAwsS3());
        assertFalse("Info1 is an aws s3 with virtual host", info1.isVirtualHostBuckets());
        assertEquals(getUrl(), info1.getS3Host());
        assertEquals(getUrl(), info2.getS3Host());

    }

    public void asserting(S3ServiceInfo info1, S3ServiceInfo info2) {
        assertServiceFoundOfType(info1, S3ServiceInfo.class);
        assertServiceFoundOfType(info2, S3ServiceInfo.class);

        assertEquals(bucketName1, info1.getBucket());
        assertEquals(bucketName2, info2.getBucket());

        assertEquals(accessKeyId, info1.getAccessKeyId());
        assertEquals(accessKeyId, info2.getAccessKeyId());

        assertEquals(secretAccessKey, info1.getSecretAccessKey());
        assertEquals(secretAccessKey, info2.getSecretAccessKey());
    }

    public void assertingUrlEncode(S3ServiceInfo info1, S3ServiceInfo info2) {
        assertServiceFoundOfType(info1, S3ServiceInfo.class);
        assertServiceFoundOfType(info2, S3ServiceInfo.class);

        assertEquals(bucketName1, info1.getBucket());
        assertEquals(bucketName2, info2.getBucket());

        assertEquals(accessKeyIdUrlEncodeDecoded, info1.getAccessKeyId());
        assertEquals(accessKeyIdUrlEncodeDecoded, info2.getAccessKeyId());

        assertEquals(secretAccessKeyUrlEncodeDecoded, info1.getSecretAccessKey());
        assertEquals(secretAccessKeyUrlEncodeDecoded, info2.getSecretAccessKey());
    }

    @Test
    public void s3ServiceCreationWithLabelNoTags() {
        when(this.mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(getServicesPayload(this.getS3ServicePayloadWithLabelNoTags("s3-1", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName1),
                this.getS3ServicePayloadWithLabelNoTags("s3-2", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName2)));
        List<ServiceInfo> serviceInfos = this.testCloudConnector.getServiceInfos();
        S3ServiceInfo info1 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-1");
        S3ServiceInfo info2 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-2");
        this.asserting(info1, info2);
        assertTrue("Info1 is an aws s3", !info1.isAwsS3());
        assertTrue("Info2 is an aws s3", !info2.isAwsS3());
        assertFalse("Info1 is an aws s3 with virtual host", info1.isVirtualHostBuckets());
        assertFalse("Info2 is an aws s3 with virtual host", info2.isVirtualHostBuckets());
        assertEquals(getUrl(), info1.getS3Host());
        assertEquals(getUrl(), info2.getS3Host());
    }

    @Test
    public void s3ServiceCreationNoLabelNoTags() {
        String hostNameDns = "mys3.com";
        when(this.mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(getServicesPayload(this.getS3ServicePayloadNoLabelNoTags("s3-1", hostNameDns, 80, accessKeyId, secretAccessKey, bucketName1),
                this.getS3ServicePayloadNoLabelNoTags("s3-2", hostNameDns, 80, accessKeyId, secretAccessKey, bucketName2)));
        List<ServiceInfo> serviceInfos = this.testCloudConnector.getServiceInfos();
        S3ServiceInfo info1 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-1");
        S3ServiceInfo info2 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-2");
        this.asserting(info1, info2);
        assertTrue("Info1 is an aws s3", !info1.isAwsS3());
        assertTrue("Info2 is an aws s3", !info2.isAwsS3());
        assertTrue("Info1 is not an aws s3 with virtual host", info1.isVirtualHostBuckets());
        assertTrue("Info2 is not an aws s3 with virtual host", info2.isVirtualHostBuckets());
        assertEquals("https://" + bucketName1 + "." + hostNameDns + ":" + 80, info1.getS3Host());
        assertEquals("https://" + bucketName2 + "." + hostNameDns + ":" + 80, info2.getS3Host());
    }

    @Test
    public void AwsS3ServiceCreationNoLabelNoTags() {
        when(this.mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(getServicesPayload(this.getAwsS3ServicePayloadNoLabelNoTags("s3-1", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName1),
                this.getAwsS3ServicePayloadNoLabelNoTags("s3-2", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName2)));
        List<ServiceInfo> serviceInfos = this.testCloudConnector.getServiceInfos();
        S3ServiceInfo info1 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-1");
        S3ServiceInfo info2 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-2");
        this.asserting(info1, info2);

        assertTrue("Info1 is not an aws s3", info1.isAwsS3());
        assertTrue("Info2 is not an aws s3", info2.isAwsS3());

        assertFalse("Info1 is an aws s3 with virtual host", info1.isVirtualHostBuckets());
        assertFalse("Info2 is an aws s3 with virtual host", info2.isVirtualHostBuckets());

        assertEquals("http://s3.amazonaws.com", info1.getS3Host());
        assertEquals("http://s3.amazonaws.com", info2.getS3Host());
    }

    @Test
    public void AwsS3ServiceCreation() {

        when(this.mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(getServicesPayload(this.getAwsS3ServicePayload("s3-1", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName1),
                this.getAwsS3ServicePayload("s3-2", "10.20.30.40", 80, accessKeyId, secretAccessKey, bucketName2)));
        List<ServiceInfo> serviceInfos = this.testCloudConnector.getServiceInfos();
        S3ServiceInfo info1 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-1");
        S3ServiceInfo info2 = (S3ServiceInfo) getServiceInfo(serviceInfos, "s3-2");
        this.asserting(info1, info2);
        assertTrue("Info1 is not an aws s3", info1.isAwsS3());
        assertTrue("Info2 is not an aws s3", info2.isAwsS3());

        assertTrue("Info1 is not an aws s3 with virtual host", info1.isVirtualHostBuckets());
        assertTrue("Info2 is not an aws s3 with virtual host", info2.isVirtualHostBuckets());

        assertEquals("http://" + bucketName1 + ".s3-aws-region.amazonaws.com", info1.getS3Host());
        assertEquals("http://" + bucketName2 + ".s3-aws-region.amazonaws.com", info2.getS3Host());
    }

    private String getRelationalPayload(String templateFile, String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        String payload = this.readTestDataFile(templateFile);
        payload = payload.replace("$serviceName", serviceName);
        payload = payload.replace("$hostname", hostname);
        payload = payload.replace("$port", Integer.toString(port));
        payload = payload.replace("$access_key_id", accessKeyId);
        payload = payload.replace("$secret_access_key", secretAccessKey);
        payload = payload.replace("$bucketName", bucketName);
        return payload;
    }

    private String getS3ServicePayload(String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        return this.getRelationalPayload("test-s3-info.json", serviceName, hostname, port, accessKeyId, secretAccessKey, bucketName);
    }

    private String getS3ServicePayloadWithUrlEncode(String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        return this.getRelationalPayload("test-s3-info.json", serviceName, hostname, port, accessKeyId, secretAccessKey, bucketName);
    }

    private String getAwsS3ServicePayload(String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        return this.getRelationalPayload("test-s3-aws-info.json", serviceName, hostname, port, accessKeyId, secretAccessKey, bucketName);
    }

    private String getS3ServicePayloadWithLabelNoTags(String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        return this.getRelationalPayload("test-s3-info-with-label-no-tags.json", serviceName, hostname, port, accessKeyId, secretAccessKey, bucketName);
    }

    private String getS3ServicePayloadNoLabelNoTags(String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        return this.getRelationalPayload("test-s3-info-no-label-no-tags.json", serviceName, hostname, port, accessKeyId, secretAccessKey, bucketName);
    }

    private String getAwsS3ServicePayloadNoLabelNoTags(String serviceName, String hostname, int port, String accessKeyId, String secretAccessKey, String bucketName) {
        return this.getRelationalPayload("test-s3-aws-info-no-label-no-tags.json", serviceName, hostname, port, accessKeyId, secretAccessKey, bucketName);
    }
}
