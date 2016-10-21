package org.springframework.cloud.cloudfoundry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.AbstractCloudConnector;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.FallbackServiceInfoCreator;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnector extends AbstractCloudConnector<Map<String,Object>> {

	private ObjectMapper objectMapper = new ObjectMapper();
	private EnvironmentAccessor environment = new EnvironmentAccessor();
	
	private ApplicationInstanceInfoCreator applicationInstanceInfoCreator = new ApplicationInstanceInfoCreator();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CloudFoundryConnector() {
		super((Class) CloudFoundryServiceInfoCreator.class);
	}

	@Override
	public boolean isInMatchingCloud() {
		return environment.getEnvValue("VCAP_APPLICATION") != null;
	}
	
	@Override
	public ApplicationInstanceInfo getApplicationInstanceInfo() {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> rawApplicationInstanceInfo 
				= objectMapper.readValue(environment.getEnvValue("VCAP_APPLICATION"), Map.class);
			return applicationInstanceInfoCreator.createApplicationInstanceInfo(rawApplicationInstanceInfo);
		} catch (Exception e) {
			throw new CloudException(e);
		} 
	}
	
	/* package for testing purpose */
	void setCloudEnvironment(EnvironmentAccessor environment) {
		this.environment = environment;
	}

	
	/**
	 * Return object representation of the VCAP_SERVICES environment variable
	 * <p>
	 * Returns a list whose element is a map with service attributes. 
	 * </p>
	 * @return parsed service data
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> getServicesData() {
		String servicesString = environment.getEnvValue("VCAP_SERVICES");
		Map<String, List<Map<String,Object>>> rawServices = new HashMap<String, List<Map<String,Object>>>();
		
		if (servicesString != null && servicesString.length() > 0) {
			try {
				rawServices = objectMapper.readValue(servicesString, Map.class);
			} catch (Exception e) {
				throw new CloudException(e);
			}
		}

		List<Map<String,Object>> flatServices = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, List<Map<String,Object>>> entry : rawServices.entrySet()) {
			flatServices.addAll(entry.getValue());
		}
		return flatServices;
	}
	

	@Override
	protected FallbackServiceInfoCreator<BaseServiceInfo,Map<String,Object>> getFallbackServiceInfoCreator() {
		return new CloudFoundryFallbackServiceInfoCreator();
	}
}

class CloudFoundryFallbackServiceInfoCreator extends FallbackServiceInfoCreator<BaseServiceInfo,Map<String,Object>> {
	@Override
	public BaseServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		String id = (String) serviceData.get("name");
		return new BaseServiceInfo(id);
	}
}
