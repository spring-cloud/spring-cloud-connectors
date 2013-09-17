package org.springframework.cloud.heroku;

import java.util.Map;

import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class HerokuServiceInfoCreator<T extends ServiceInfo> implements ServiceInfoCreator<T> {

	private String urlProtocol;

	public HerokuServiceInfoCreator(String urlProtocol) {
		this.urlProtocol = urlProtocol;
	}
	
	public boolean accept(Object serviceData) {
		@SuppressWarnings("unchecked")
		Map.Entry<String,Object> serviceDataEntry = (Map.Entry<String,Object>)serviceData;
		
		return serviceDataEntry.getValue().toString().startsWith(urlProtocol + "://");
	}
}
