package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.app.BasicApplicationInstanceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class ApplicationInstanceInfoCreator {
	public ApplicationInstanceInfo createApplicationInstanceInfo(Map<String, Object> applicationInstanceData) {
		String instanceId = (String) applicationInstanceData.get("instance_id");
		String appId = (String) applicationInstanceData.get("name"); 

		return new BasicApplicationInstanceInfo(instanceId, appId, applicationInstanceData);
	}
}
