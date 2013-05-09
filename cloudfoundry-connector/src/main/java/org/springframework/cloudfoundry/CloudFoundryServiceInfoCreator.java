package org.springframework.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class CloudFoundryServiceInfoCreator<T extends ServiceInfo> implements ServiceInfoCreator<T> {

	private String label;

	public CloudFoundryServiceInfoCreator(String label) {
		this.label = label;
	}
	
	public boolean accept(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> serviceDataMap = (Map<String,Object>)serviceData;
		
		return labelWithoutVersion(serviceDataMap.get("label").toString()).equals(label);
	}
	
	protected static String labelWithoutVersion(String labelWithVersion) {
		int hyphenIndex = labelWithVersion.lastIndexOf('-');
		if (hyphenIndex == -1) {
			return labelWithVersion;
		} else {
			return labelWithVersion.substring(0, hyphenIndex);
		}
	}

}
