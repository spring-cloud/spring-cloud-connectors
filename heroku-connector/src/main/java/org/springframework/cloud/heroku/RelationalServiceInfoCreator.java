package org.springframework.cloud.heroku;

import java.util.Map;

import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends HerokuServiceInfoCreator<SI> {

	public RelationalServiceInfoCreator(String urlProtocol) {
		super(urlProtocol);
	}

	public abstract SI createServiceInfo(String id, String uri);
	
	public SI createServiceInfo(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map.Entry<String,String> serviceDataEntry = (Map.Entry<String,String>)serviceData;
		
		return createServiceInfo(serviceDataEntry.getKey(), serviceDataEntry.getValue());
	}
}
