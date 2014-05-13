package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.MonitoringServiceInfo;

/**
 * @author Ramnivas Laddad
 */
public class MonitoringServiceInfoCreator extends CloudFoundryServiceInfoCreator<MonitoringServiceInfo> {
	public MonitoringServiceInfoCreator() {
		super(new Tags("monitoring", "newrelic"));
	}

	// Until NewRelic service payload contains tags, we have to go with overriding to check the label
	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return labelStartsWithTag(serviceData);
	}

	@Override
	public MonitoringServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");
		return new MonitoringServiceInfo(id);
	}
}