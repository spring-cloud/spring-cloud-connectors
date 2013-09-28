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
public abstract class CloudFoundryServiceInfoCreator<SI extends ServiceInfo> implements ServiceInfoCreator<SI, Map<String,Object>> {

	private String tag;

	public CloudFoundryServiceInfoCreator(String tag) {
		this.tag = tag;
	}
	
	@SuppressWarnings("unchecked")
	public boolean accept(Map<String,Object> serviceData) {
		return ((List<String>)serviceData.get("tags")).contains(tag);
	}
}
