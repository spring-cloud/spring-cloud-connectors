package org.springframework.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.app.ApplicationInstanceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class ApplicationInstanceInfoCreator {
	public ApplicationInstanceInfo createApplicationInstanceInfo(Map<String, Object> applicationInstanceData) {
		String instanceId = (String) applicationInstanceData.get("instance_id");
		String appId = (String) applicationInstanceData.get("name"); 

		return new CloudFoundryApplicationInstanceInfo(instanceId, appId, applicationInstanceData);
	}
}
