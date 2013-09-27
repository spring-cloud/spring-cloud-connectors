package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.NewRelicServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class NewRelicServiceInfoCreator extends CloudFoundryServiceInfoCreator<NewRelicServiceInfo> {
    public NewRelicServiceInfoCreator() {
        super("newrelic");
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
    public NewRelicServiceInfo createServiceInfo(Object serviceData) {
        Map<String, Object> serviceDataMap = (Map<String, Object>) serviceData;

        String id = (String) serviceDataMap.get("name");
        return new NewRelicServiceInfo(id);
    }
}