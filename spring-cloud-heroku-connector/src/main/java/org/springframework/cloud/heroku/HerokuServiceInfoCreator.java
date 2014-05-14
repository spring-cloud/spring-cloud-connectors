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
	
	/**
	 * Get prefixes for env variable with which the associated {@link ServiceInfo} may be created.
	 * 
	 * Unlike CloudFoundry which exposes VCAP_SERVICES as a single environment to encompass all services bound
	 * to the app, Heroku expose one environment variable per app. This method allows each info creator to declare
	 * appropriate env variables. 
	 * 
	 * @return prefixes for the relevant environment variables 
	 */
	public abstract String[] getEnvPrefixes();
}
