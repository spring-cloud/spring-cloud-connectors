package org.springframework.cloud.heroku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.AbstractCloudConnector;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.service.ServiceInfo;

/**
 * Implementation of CloudConnector for Heroku
 * 
 * Currently support only the Postgres service.
 * 
 * @author Ramnivas Laddad
 *
 */
public class HerokuConnector extends AbstractCloudConnector {

	private EnvironmentAccessor environment = new EnvironmentAccessor();
	private ApplicationInstanceInfoCreator applicationInstanceInfoCreator 
		= new ApplicationInstanceInfoCreator(environment);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HerokuConnector() {
		super((Class) HerokuServiceInfoCreator.class);
	}

	@Override
	public boolean isInMatchingCloud() {
		return environment.getValue("DYNO") != null;
	}
	
	@Override
	public ApplicationInstanceInfo getApplicationInstanceInfo() {
		try {
			return applicationInstanceInfoCreator.createApplicationInstanceInfo();
		} catch (Exception e) {
			throw new CloudException(e);
		} 
	}
	
	@Override
	public List<ServiceInfo> getServiceInfos() {
		List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
		for (Map.Entry<String,String> serviceData : getServicesData().entrySet()) {
			serviceInfos.add(getServiceInfo(serviceData));
		}
		
		return serviceInfos;
	}
	
	/* package for testing purpose */
	void setCloudEnvironment(EnvironmentAccessor environment) {
		this.environment = environment;
		this.applicationInstanceInfoCreator = new ApplicationInstanceInfoCreator(environment);
	}

	private ServiceInfo getServiceInfo(Map.Entry<String, String> serviceData) {
		for (ServiceInfoCreator<?> serviceInfoCreator : serviceInfoCreators) {
			if (serviceInfoCreator.accept(serviceData)) {
				return serviceInfoCreator.createServiceInfo(serviceData);
			}
		}
		
		throw new CloudException("No suitable service info creator found");
	}

	/**
	 * Return object representation of the bound services
	 * <p>
	 * Returns map whose key is the env key and value is the associated url
	 * </p>
	 * @return
	 */
	private Map<String,String> getServicesData() {
		Map<String,String> serviceData = new HashMap<String,String>();
		
		Map<String,String> env = environment.getEnv();
		
		for (Map.Entry<String, String> envEntry : env.entrySet()) {
			if (envEntry.getKey().startsWith("HEROKU_POSTGRESQL_")) {
				serviceData.put(envEntry.getKey(), envEntry.getValue());
			}
		}

		return serviceData;
	}
	
}
