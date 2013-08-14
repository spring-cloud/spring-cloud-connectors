package org.springframework.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class CloudFoundryServiceInfoCreator<T extends ServiceInfo> implements ServiceInfoCreator<T> {

	private String tag;

	public CloudFoundryServiceInfoCreator(String tag) {
		this.tag = tag;
	}
	
	@SuppressWarnings("unchecked")
	public boolean accept(Object serviceData) {
		Map<String,Object> serviceDataMap = (Map<String,Object>)serviceData;
		
		return ((List<String>)serviceDataMap.get("tags")).contains(tag);
	}
}
