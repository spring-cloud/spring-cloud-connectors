package org.springframework.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arthur Halet
 */
public class S3ServiceInfo extends UriBasedServiceInfo {

    public final static String S3_SCHEME = "s3";
    private static final String AWS_S3_PATTERN = "^((([^\\.])*)\\.)?s3([^\\.])*\\.amazonaws\\.com$";
    private static final String VIRTUAL_HOST_BUCKETS_PATTERN = "^([^\\.]*)";
    private String bucket;

    public S3ServiceInfo(String id, String scheme, String host, int port, String username, String password, String path) {
        super(id, scheme, host, port, username, password, path);
        this.extractBucket();
    }

    public S3ServiceInfo(String id, String uriString) {
        super(id, uriString);
        this.extractBucket();
    }

    private void extractBucket() {
        if (!this.isVirtualHostBuckets()) {
            this.bucket = this.getPath();
            return;
        }
        String bucketFromHost = this.getBucketFromVirtualHost();
        if (bucketFromHost != null && !bucketFromHost.isEmpty()) {
            this.bucket = bucketFromHost;
            return;
        }
        this.bucket = this.getPath();
    }

    public String getS3Host() {
        if (this.isAwsS3()) {
            return this.getS3AwsHost();
        }
        String port = "";
        if (this.getPort() != -1) {
            port += ":" + this.getPort();
        }
        String protocol = this.getProtocol();
        return protocol + "://" + this.getHost() + port;
    }

    public String getS3AwsHost() {
        String protocol = this.getProtocol();
        return protocol + "://" + this.getHost();
    }

    public String getBucketFromVirtualHost() {
        if (!this.isVirtualHostBuckets()) {
            return null;
        }
        Pattern pattern = Pattern.compile(VIRTUAL_HOST_BUCKETS_PATTERN);
        Matcher matcher = pattern.matcher(this.getHost());
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    public boolean isVirtualHostBuckets() {
        if ((this.getPath() == null || this.getPath().isEmpty())
                && (this.getHost().split("\\.").length >= 3)) {
            return true;
        }
        return false;
    }

    private String getProtocol() {
        String protocol = this.getScheme();
        if (protocol.equals("s3") && !this.isAwsS3()) {
            protocol = "https";
        } else if (protocol.equals("s3") && this.isAwsS3()) {
            protocol = "http";
        }
        return protocol;
    }

    public String getAccessKeyId() {
        return this.getUserName();
    }

    public String getSecretAccessKey() {
        return this.getPassword();
    }

    public boolean isAwsS3() {
        return this.getHost().matches(AWS_S3_PATTERN);
    }


    public String getBucket() {
        return this.bucket;
    }
}