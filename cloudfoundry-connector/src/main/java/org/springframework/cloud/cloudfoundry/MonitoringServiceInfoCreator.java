package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.MonitoringServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MonitoringServiceInfoCreator extends CloudFoundryServiceInfoCreator<MonitoringServiceInfo> {
    public MonitoringServiceInfoCreator() {
        super("monitoring");
    }

    // Until NewRelic service payload contains tags, we have to go with overriding to check the label
    @Override
    public boolean accept(Object serviceData) {
        @SuppressWarnings("unchecked")
        Map<String, Object> serviceDataMap = (Map<String, Object>) serviceData;

        return ((String) serviceDataMap.get("label")).startsWith("newrelic");
    }

    @Override
    @SuppressWarnings("unchecked")
    public MonitoringServiceInfo createServiceInfo(Object serviceData) {
        Map<String, Object> serviceDataMap = (Map<String, Object>) serviceData;

        String id = (String) serviceDataMap.get("name");
        return new MonitoringServiceInfo(id);
    }
}