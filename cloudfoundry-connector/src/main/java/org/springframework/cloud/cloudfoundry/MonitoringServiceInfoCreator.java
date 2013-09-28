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
    public boolean accept(Map<String,Object> serviceData) {
        return ((String) serviceData.get("label")).startsWith("newrelic");
    }

    @Override
    public MonitoringServiceInfo createServiceInfo(Map<String,Object> serviceData) {
        String id = (String) serviceData.get("name");
        return new MonitoringServiceInfo(id);
    }
}