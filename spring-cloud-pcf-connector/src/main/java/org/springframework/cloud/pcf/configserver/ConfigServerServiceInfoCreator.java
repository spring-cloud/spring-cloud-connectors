package org.springframework.cloud.pcf.configserver;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;

/**
 * Service info creator for Config Server services
 *
 * @author Chris Schaefer
 */
public class ConfigServerServiceInfoCreator extends CloudFoundryServiceInfoCreator<ConfigServerServiceInfo> {
	private static final String CREDENTIALS_ID_KEY = "name";
	private static final String CONFIG_SERVER_SERVICE_TAG_NAME = "configuration";

	public ConfigServerServiceInfoCreator() {
		super(new Tags(CONFIG_SERVER_SERVICE_TAG_NAME));
	}

	@Override
	public ConfigServerServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		String uri = getUriFromCredentials(getCredentials(serviceData));

		return new ConfigServerServiceInfo(id, uri);
	}
}
