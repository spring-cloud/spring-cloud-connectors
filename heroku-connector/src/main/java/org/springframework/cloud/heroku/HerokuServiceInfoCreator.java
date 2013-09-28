package org.springframework.cloud.heroku;

import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.heroku.HerokuConnector.KeyValuePair;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class HerokuServiceInfoCreator<SI extends ServiceInfo> implements ServiceInfoCreator<SI,KeyValuePair> {

	private String urlProtocol;

	public HerokuServiceInfoCreator(String urlProtocol) {
		this.urlProtocol = urlProtocol;
	}
	
	public boolean accept(KeyValuePair serviceData) {
		return serviceData.getValue().toString().startsWith(urlProtocol + "://");
	}
	
	public abstract SI createServiceInfo(String id, String uri);
	
	public SI createServiceInfo(KeyValuePair serviceData) {
		return createServiceInfo(serviceData.getKey(), serviceData.getValue());
	}
}
