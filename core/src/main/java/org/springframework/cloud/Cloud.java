package org.springframework.cloud;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataSource;

import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.ServiceInfo.ServiceProperty;

/**
 * The main user-level API access to application and services for the app in which this instance is embedded in.
 * 
 * The class connects application and the underlying cloud. Besides passing along information about the application 
 * instance, it allows simple access for using services. It uses {@link ServiceInfo}s obtained using the 
 * underlying {@link CloudConnector} and allows translating those to service connector such as a {@link DataSource}.
 *  
 * It also passes along information obtained through {@link CloudConnector} to let application take control on how
 * to use bound services.
 * 
 * <p>NOTE: Users or cloud providers shouldn't need to instantiate an instance of this class 
 * (constructor has package-access only for unit-testing purpose). Instead, they can obtain an appropriate 
 * instance through {@link CloudFactory}</p>
 * 
 * @author Ramnivas Laddad
 *
 */
public class Cloud {
	private CloudConnector cloudConnector;
	private ServiceConnectorCreatorRegistry serviceConnectorCreatorRegistry = new ServiceConnectorCreatorRegistry();
	
	/**
	 * Package-access constructor.
	 * 
	 * @param cloudConnector the underlying connector
	 * @param serviceConnectorCreators service connector creators
	 */
	Cloud(CloudConnector cloudConnector, List<ServiceConnectorCreator<?, ? extends ServiceInfo>> serviceConnectorCreators) {
		this.cloudConnector = cloudConnector;
		
		for (ServiceConnectorCreator<?, ? extends ServiceInfo> serviceCreator: serviceConnectorCreators) {
			registerServiceConnectorCreator(serviceCreator);			
		}
	}
	
	/**
	 * @see CloudConnector#getApplicationInstanceInfo()
	 * 
	 * @return
	 */
	public ApplicationInstanceInfo getApplicationInstanceInfo() {
		return cloudConnector.getApplicationInstanceInfo();
	}
	
	/**
	 * Get {@link ServiceInfo} for the given service id
	 * 
	 * @param serviceId service id
	 * @return info for the serviceId
	 */
	public ServiceInfo getServiceInfo(String serviceId) {
		for (ServiceInfo serviceInfo : getServiceInfos()) {
			if (serviceInfo.getId().equals(serviceId)) {
				return serviceInfo;
			}
		}
		throw new CloudException("No service with id " + serviceId + " found");
	}
	
	/**
	 * @see CloudConnector#getServiceInfos()
	 * @return
	 */
	public List<ServiceInfo> getServiceInfos() {
		return cloudConnector.getServiceInfos();
	}
	
	/**
	 * Get {@link ServiceInfo}s for the bound services that could be mapped to the given service connector type.
	 * 
	 * <p>
	 * For example, if the connector type is {@link DataSource}, then the method will return 
	 * all {@link ServiceInfo} objects matching bound relational database services.
	 * <p> 
	 * 
	 * @param serviceConnectorType service connector type. 
	 *                             Passing null returns all {@link ServiceInfo}s (matching that of {@link Cloud#getServiceInfo()} 
	 * @return
	 */
	public <T> List<ServiceInfo> getServiceInfos(Class<T> serviceConnectorType) {
		List<ServiceInfo> allServiceInfos = getServiceInfos();
		List<ServiceInfo> matchingServiceInfos = new ArrayList<ServiceInfo>();
		
		for (ServiceInfo serviceInfo: allServiceInfos) {
			if (serviceConnectorCreatorRegistry.getServiceCreator(serviceConnectorType, serviceInfo) != null) {
				matchingServiceInfos.add(serviceInfo);
			}
		}
		
		return matchingServiceInfos;
	}
	
	/**
	 * Get a service connector for the given service id, the connector type, configured with the given config
	 * 
	 * @param serviceId the service id 
	 * @param serviceConnectorType The expected class of service connector such as, DataSource.class.
	 * @param serviceConnectorConfig service connector configuration (such as pooling parameters).
	 * 
	 */
	public <SC> SC getServiceConnector(String serviceId, Class<SC> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfig) {
		ServiceInfo serviceInfo = getServiceInfo(serviceId);
		
		return getServiceConnector(serviceInfo, serviceConnectorType, serviceConnectorConfig);
	}

	/**
	 * Get the singleton service connector for the given connector type, configured with the given config
	 * 

	 * @param serviceConnectorType The expected class of service connector such as, DataSource.class.
	 * @param serviceConnectorConfig service connector configuration (such as pooling parameters).
	 * 
	 */
	public <SC> SC getSingletonServiceConnector(Class<SC> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfig) {
		List<ServiceInfo> matchingServiceInfos = getServiceInfos(serviceConnectorType);
		
		if (matchingServiceInfos.size() != 1) {
			throw new CloudException("No unique service matching " + serviceConnectorType + " found. Expected 1, found " + matchingServiceInfos.size());
		}
		
		ServiceInfo matchingServiceInfo = matchingServiceInfos.get(0);
		
		return getServiceConnector(matchingServiceInfo, serviceConnectorType, serviceConnectorConfig);
	}

	/**
	 * Register a new service connector creator
	 * 
	 * @param serviceConnectorCreator
	 */
	public void registerServiceConnectorCreator(ServiceConnectorCreator<?, ? extends ServiceInfo> serviceConnectorCreator) {
		serviceConnectorCreatorRegistry.registerCreator(serviceConnectorCreator);
	}

	/**
	 * Get properties for app and services.
	 * 
	 * 
	 * 
	 * <p>Application properties always include <code>cloud.application.app-id</code>
	 * and <code>cloud.application.instance-id</code> with values bound to application id and
	 * instance id. The rest of the properties are cloud-provider specific, but take the 
	 * <code>cloud.application.&lt;property-name&gt;</code> form.
	 * <code><pre>
	 * cloud.application.app-id = helloworld
	 * cloud.application.instance-id = instance-0-0fab098f
	 * cloud.application.&lt;property-name&gt; = &lt;property-value&gt;
	 * </pre></code>
	 *
	 * <p>Service specific properties are exposed for each bound service, with each key starting
	 * in <code>cloud.services</code>. Like application properties, these too are cloud and service specific.
	 * Each key for a specific service starts with <code>cloud.services.&lt;service-id&gt;</code>
	 * <code><pre>
	 * cloud.services.customerDb.type = mysql-5.1
	 * cloud.services.customerDb.plan = free
	 * cloud.services.customerDb.connection.hostname = ...
	 * cloud.services.customerDb.connection.port = ...
	 * etc...
	 * </pre></code>
	 *
	 * <p>If a there is only a single service of a given type, that service is
	 * aliased to the service type. Keys for such properties start in <code>cloud.services.&lt;service-type&gt;</code>.
	 * For example, if there is only a single MySQL service bound to the application, the service properties will also be
	 * exposed starting with '<code>cloud.services.mysql</code>' key:
	 * <code><pre>
	 * cloud.services.mysql.type = mysql-5.1
	 * cloud.services.mysql.plan = free
	 * cloud.services.mysql.connection.hostname = ...
	 * cloud.services.mysql.connection.port = ...
	 * etc...
	 * </pre></code>
	 * 
	 * @return the properties object
	 */
	public Properties getCloudProperties() {
		Map<String, List<ServiceInfo>> mappedServiceInfos = new HashMap<String, List<ServiceInfo>>();
		for (ServiceInfo serviceInfo : getServiceInfos()) {
			String key = getServiceLabel(serviceInfo);
			List<ServiceInfo> serviceInfosForLabel = mappedServiceInfos.get(key);
			if (serviceInfosForLabel == null) {
				serviceInfosForLabel = new ArrayList<ServiceInfo>();
				mappedServiceInfos.put(key, serviceInfosForLabel);
			}
			serviceInfosForLabel.add(serviceInfo);
		}
		
		final String servicePropKeyLead = "cloud.services.";
		Properties cloudProperties = new Properties();
		for (Entry<String, List<ServiceInfo>> mappedServiceInfo : mappedServiceInfos.entrySet()) {
			List<ServiceInfo> serviceInfos = mappedServiceInfo.getValue();
			
			for (ServiceInfo serviceInfo : serviceInfos) {
				String idBasedKey = servicePropKeyLead + serviceInfo.getId();
				cloudProperties.putAll(getServiceProperties(idBasedKey, serviceInfo));
				
				// If there is only one service for a given label, put props with that label instead of just id
				if (serviceInfos.size() == 1) {
					String labelBasedKey = servicePropKeyLead + mappedServiceInfo.getKey();
					cloudProperties.putAll(getServiceProperties(labelBasedKey, serviceInfo));
				}
			}
		}
		
		cloudProperties.putAll(getAppProperties());
		
		return cloudProperties;
	}
	
	private <SC> SC getServiceConnector(ServiceInfo serviceInfo, Class<SC> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfig) {
		ServiceConnectorCreator<SC, ServiceInfo> serviceConnectorCreator = serviceConnectorCreatorRegistry.getServiceCreator(serviceConnectorType, serviceInfo);
		return serviceConnectorCreator.create(serviceInfo, serviceConnectorConfig);
	}
	
	private Properties getAppProperties() {
		final String appPropLeadKey = "cloud.application.";
		
		Properties appProperties = new Properties();
		appProperties.put(appPropLeadKey + "instance-id", getApplicationInstanceInfo().getInstanceId());
		appProperties.put(appPropLeadKey + "app-id", getApplicationInstanceInfo().getAppId());
		for (Map.Entry<String, Object> entry : getApplicationInstanceInfo().getProperties().entrySet()) {
			if (entry.getValue() != null) {
				appProperties.put(appPropLeadKey + entry.getKey(), entry.getValue());
			}
		}

		return appProperties;
	}
	
	private Properties getServiceProperties(String keyLead, ServiceInfo serviceInfo) {
		Properties cloudProperties = new Properties();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(serviceInfo.getClass());
			PropertyDescriptor[] propDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propDescriptor : propDescriptors) {
				ServiceProperty propAnnotation = propDescriptor.getReadMethod().getAnnotation(ServiceProperty.class);
				String key = keyLead;
				
				if (propAnnotation != null) {
					if (!propAnnotation.category().isEmpty()) {
						key = key + "." + propAnnotation.category();
					}
					if (!propAnnotation.name().isEmpty()) {
						key = key + "." + propAnnotation.name();
					} else {
						key = key + "." + propDescriptor.getName().toLowerCase();
					}
					
					Object value = propDescriptor.getReadMethod().invoke(serviceInfo);
					
					if (value != null) {
						cloudProperties.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			throw new CloudException(e);
		}
		
		return cloudProperties;
	}
	
	private static String getServiceLabel(ServiceInfo serviceInfo) {
		Class<? extends ServiceInfo> serviceInfoClass = serviceInfo.getClass();
		
		ServiceLabel labelAnnotation = serviceInfoClass.getAnnotation(ServiceInfo.ServiceLabel.class);
		
		if (labelAnnotation == null) {
			return null;
		} else {
			return labelAnnotation.value();
		}
	}
}

class ServiceConnectorCreatorRegistry {
	private List<ServiceConnectorCreator<?, ? extends ServiceInfo>> serviceConnectorCreators = new ArrayList<ServiceConnectorCreator<?, ? extends ServiceInfo>>();
	
	public void registerCreator(ServiceConnectorCreator<?, ? extends ServiceInfo> serviceConnectorCreator) {
		serviceConnectorCreators.add(serviceConnectorCreator);
	}
	
	@SuppressWarnings("unchecked")
	public <SC, SI extends ServiceInfo> ServiceConnectorCreator<SC, SI> getServiceCreator(Class<SC> serviceConnectorType, SI serviceInfo) {
		for (ServiceConnectorCreator<?, ? extends ServiceInfo> serviceConnectorCreator : serviceConnectorCreators) {
			if (accept(serviceConnectorCreator, serviceConnectorType, serviceInfo)) {
				return (ServiceConnectorCreator<SC, SI>) serviceConnectorCreator;
			}
		}
		return null;
	}
	
	public boolean accept(ServiceConnectorCreator<?, ? extends ServiceInfo> creator, Class<?> serviceConnectorType, ServiceInfo serviceInfo) {
		boolean typeBasedAccept = serviceConnectorType == null ? true : serviceConnectorType.isAssignableFrom(creator.getServiceConnectorType());
		boolean infoBasedAccept = serviceInfo == null ? true : serviceInfo.getClass().isAssignableFrom(creator.getServiceInfoType());
		
		return  typeBasedAccept && infoBasedAccept;
	}
}
