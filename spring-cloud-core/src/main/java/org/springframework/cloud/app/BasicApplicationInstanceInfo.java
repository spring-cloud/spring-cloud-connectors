package org.springframework.cloud.app;

import java.util.Map;

import org.springframework.cloud.app.ApplicationInstanceInfo;

/**
 * Basic implementation of ApplicationInstanceInfo that might suffice most typical
 * cloud connectors.
 * 
 * @author Ramnivas Laddad
 *
 */
public class BasicApplicationInstanceInfo implements ApplicationInstanceInfo {

	private String instanceId;
	private String appId;
	private Map<String, Object> properties;

	public BasicApplicationInstanceInfo(String instanceId, String appId, Map<String, Object> properties) {
		this.instanceId = instanceId;
		this.appId = appId;
		this.properties = properties;
	}
	
	@Override
	public String getInstanceId() {
		return instanceId;
	}

	@Override
	public String getAppId() {
		return appId;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

}
