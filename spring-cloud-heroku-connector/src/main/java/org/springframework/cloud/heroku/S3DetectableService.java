package org.springframework.cloud.heroku;

/**
 * @author Arthur Halet
 */
public enum S3DetectableService {
    Buckeeter("BUCKETEER_BUCKET_NAME", "BUCKETEER_BUCKET_NAME", "BUCKETEER_AWS_ACCESS_KEY_ID", "BUCKETEER_AWS_SECRET_ACCESS_KEY", "https://$bucketName.s3.amazonaws.com"),
    AWS("S3_BUCKET_NAME", "S3_BUCKET_NAME", "AWS_ACCESS_KEY_ID", "AWS_SECRET_ACCESS_KEY", "https://$bucketName.s3.amazonaws.com");
    private String detectEnvKey;
    private String bucketNameEnvKey;
    private String accessKeyIdEnvKey;
    private String secretAccessKeyEnvKey;
    private String baseUrl;


    S3DetectableService(String detectEnvKey, String bucketNameEnvKey, String accessKeyIdEnvKey, String secretAccessKeyEnvKey, String baseUrl) {
        this.detectEnvKey = detectEnvKey;
        this.bucketNameEnvKey = bucketNameEnvKey;
        this.accessKeyIdEnvKey = accessKeyIdEnvKey;
        this.secretAccessKeyEnvKey = secretAccessKeyEnvKey;
        this.baseUrl = baseUrl;
    }

    public String getDetectEnvKey() {
        return detectEnvKey;
    }

    public String getBucketNameEnvKey() {
        return bucketNameEnvKey;
    }

    public String getAccessKeyIdEnvKey() {
        return accessKeyIdEnvKey;
    }

    public String getSecretAccessKeyEnvKey() {
        return secretAccessKeyEnvKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
