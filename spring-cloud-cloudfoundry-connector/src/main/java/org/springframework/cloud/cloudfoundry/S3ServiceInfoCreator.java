package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.S3ServiceInfo;

import java.util.Map;

/**
 * @author Arthur Halet
 */
public class S3ServiceInfoCreator extends CloudFoundryServiceInfoCreator<S3ServiceInfo> {
    public S3ServiceInfoCreator() {
        super(new Tags("riak-cs", "s3", "swift", "google-storage"), "s3");
    }

    public S3ServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        Map<String, Object> credentials = this.getCredentials(serviceData);
        String id = (String) serviceData.get("name");
        String uri = this.getUriFromCredentials(credentials);
        return new S3ServiceInfo(id, uri);
    }
}
