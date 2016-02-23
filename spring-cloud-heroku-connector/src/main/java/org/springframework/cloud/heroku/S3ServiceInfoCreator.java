package org.springframework.cloud.heroku;

import org.springframework.cloud.service.UriBasedServiceData;
import org.springframework.cloud.service.common.S3ServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur Halet
 */
public class S3ServiceInfoCreator extends HerokuServiceInfoCreator<S3ServiceInfo> {

    private EnvironmentAccessor environment = new EnvironmentAccessor();

    public S3ServiceInfoCreator() {
        super(S3ServiceInfo.S3_SCHEME);
    }

    @Override
    public boolean accept(UriBasedServiceData serviceData) {
        S3DetectableService s3DetectableService = this.getDetectableService(serviceData.getKey());
        if (s3DetectableService == null) {
            return super.accept(serviceData);
        }
        return true;
    }

    @Override
    public S3ServiceInfo createServiceInfo(String id, String uri) {
        S3DetectableService s3DetectableService = this.getDetectableService(id);
        if (s3DetectableService == null) {
            return new S3ServiceInfo(HerokuUtil.computeServiceName(id), uri);
        }
        return this.createServiceInfo(s3DetectableService);
    }

    /* package for testing purpose */
    void setCloudEnvironment(EnvironmentAccessor environment) {
        this.environment = environment;
    }

    public S3ServiceInfo createServiceInfo(S3DetectableService s3DetectableService) {
        Map<String, String> env = environment.getEnv();
        String accessKeyId = env.get(s3DetectableService.getAccessKeyIdEnvKey());
        String secretAccessKey = env.get(s3DetectableService.getSecretAccessKeyEnvKey());
        String bucketName = env.get(s3DetectableService.getBucketNameEnvKey());
        String finalUrl = this.getFinalUrl(s3DetectableService, bucketName);
        URI finalUri = URI.create(finalUrl);
        return new S3ServiceInfo(bucketName, finalUri.getScheme(), finalUri.getHost(), finalUri.getPort(), accessKeyId, secretAccessKey, finalUri.getPath());
    }

    private String getFinalUrl(S3DetectableService s3DetectableService, String bucketName) {
        return s3DetectableService.getBaseUrl().replace("$bucketName", bucketName);
    }

    @Override
    public String[] getEnvPrefixes() {
        List<String> envPrefixList = new ArrayList<String>();
        envPrefixList.add("S3_URL");

        for (S3DetectableService s3DetectableService : S3DetectableService.values()) {
            envPrefixList.add(s3DetectableService.getDetectEnvKey());
        }
        String[] envPrefixes = new String[envPrefixList.size()];
        envPrefixList.toArray(envPrefixes);
        return envPrefixes;
    }

    private S3DetectableService getDetectableService(String envKey) {
        for (S3DetectableService s3DetectableService : S3DetectableService.values()) {
            if (s3DetectableService.getDetectEnvKey().equals(envKey)) {
                return s3DetectableService;
            }
        }
        return null;
    }
}

