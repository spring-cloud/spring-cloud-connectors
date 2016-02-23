package org.springframework.cloud.service.s3;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.S3ServiceInfo;

import java.util.Properties;

import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

/**
 * @author Arthur Halet
 */
public class S3FactoryCreator extends AbstractServiceConnectorCreator<S3ContextBuilder, S3ServiceInfo> {

    public S3ContextBuilder create(S3ServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
        Properties storeProviderInitProperties = new Properties();
        storeProviderInitProperties.put(PROPERTY_S3_VIRTUAL_HOST_BUCKETS, serviceInfo.isVirtualHostBuckets());
        S3ContextBuilder s3ContextBuilder = new S3ContextBuilder();
        s3ContextBuilder.getContextBuilder()
                .overrides(storeProviderInitProperties)
                .endpoint(serviceInfo.getS3Host())
                .credentials(serviceInfo.getAccessKeyId(), serviceInfo.getSecretAccessKey());
        s3ContextBuilder.setBucketName(serviceInfo.getBucket());
        return s3ContextBuilder;
    }
}
