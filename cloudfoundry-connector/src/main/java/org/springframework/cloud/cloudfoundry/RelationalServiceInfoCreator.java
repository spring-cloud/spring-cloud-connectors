package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {

	public RelationalServiceInfoCreator(String tag) {
		super(tag);
	}

	public abstract SI createServiceInfo(String id, String uri);
	
	public SI createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");
		
		String uri = (String) credentials.get("uri");

		return createServiceInfo(id, uri);
	}

}
