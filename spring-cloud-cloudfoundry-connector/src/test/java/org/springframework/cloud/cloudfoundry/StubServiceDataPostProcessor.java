package org.springframework.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

public class StubServiceDataPostProcessor implements ServiceDataPostProcessor {
	@SuppressWarnings("unchecked")
	@Override
	public CloudFoundryRawServiceData process(CloudFoundryRawServiceData serviceData) {
		for (List<Map<String, Object>> service : serviceData.values()) {
			Map<String, Object> serviceMap = service.get(0);
			String name = (String) serviceMap.get("name");
			if (name.equals("uppercase")) {
				Map<String, Object> credentials = (Map<String, Object>) serviceMap.get("credentials");
				for (Map.Entry<String, Object> entry : credentials.entrySet()) {
					String upperCaseValue = ((String) entry.getValue()).toUpperCase();
					credentials.put(entry.getKey(), upperCaseValue);
				}
			}
		}

		return serviceData;
	}
}
