package org.springframework.cloud.heroku;

import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class HerokuServiceInfoCreator<SI extends ServiceInfo> implements ServiceInfoCreator<SI> {

	private String urlProtocol;

	public HerokuServiceInfoCreator(String urlProtocol) {
		this.urlProtocol = urlProtocol;
	}
	
	public boolean accept(Object serviceData) {
		HerokuConnector.KeyValuePair serviceDataEntry = (HerokuConnector.KeyValuePair) serviceData;
		
		return serviceDataEntry.getValue().toString().startsWith(urlProtocol + "://");
	}
	
	public abstract SI createServiceInfo(String id, String uri);
	
	public SI createServiceInfo(Object serviceData) {
		HerokuConnector.KeyValuePair serviceDataEntry = (HerokuConnector.KeyValuePair)serviceData;
		
		return createServiceInfo(serviceDataEntry.getKey(), serviceDataEntry.getValue());
	}
}
