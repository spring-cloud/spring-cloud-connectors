package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.S3ServiceInfo;

/**
 * @author Arthur Halet
 */
public class S3ServiceInfoCreator extends LocalConfigServiceInfoCreator<S3ServiceInfo> {

    public S3ServiceInfoCreator() {
        super(S3ServiceInfo.S3_SCHEME);
    }

    @Override
    public S3ServiceInfo createServiceInfo(String id, String uri) {
        return new S3ServiceInfo(id, uri);
    }
}