package org.springframework.cloud.service.s3;

import org.jclouds.ContextBuilder;

/**
 * @author Arthur Halet
 */
public class S3ContextBuilder {
    private String bucketName;
    private ContextBuilder contextBuilder;

    public ContextBuilder getContextBuilder() {
        if (this.contextBuilder == null) {
            this.contextBuilder = ContextBuilder.newBuilder("s3");
        }
        return contextBuilder;
    }


    public String getBucketName() {
        return bucketName;
    }

    public S3ContextBuilder setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }
}

