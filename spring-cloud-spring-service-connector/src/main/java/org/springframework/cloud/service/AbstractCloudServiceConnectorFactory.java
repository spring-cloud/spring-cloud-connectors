package org.springframework.cloud.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.util.StringUtils;

/**
 * Abstract base factory class.
 * <p>
 * This factory uses the service creator provided through the constructor to
 * create services. If the service name is provided it creates a service object
 * based on the service bound to that name. Otherwise, it creates a singleton
 * service and fails if it doesn't find a unique service of the expected type.
 *
 * @author Ramnivas Laddad
 *
 * @param <S> The service type
 */
public abstract class AbstractCloudServiceConnectorFactory<S> extends AbstractFactoryBean<S> implements CloudServiceConnectorFactory<S> {

	private static final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";

	private Cloud cloud;

	protected String serviceId;
	private Class<? extends S> serviceConnectorType;
	private ServiceConnectorConfig serviceConnectorConfiguration;
	
	private S serviceInstance;

	/**
	 * 
	 * @param serviceId Optional service name property. If this property is	null, a unique service of the expected type 
	 * 					(redis, for example) needs to be bound to the application.
	 * @param serviceConnectorType
	 * @param serviceConnectorConfiguration
	 */
	public AbstractCloudServiceConnectorFactory(String serviceId, Class<S> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfiguration) {
		this.serviceId = serviceId;
		this.serviceConnectorType = serviceConnectorType;
		this.serviceConnectorConfiguration = serviceConnectorConfiguration;
	}

	public AbstractCloudServiceConnectorFactory(Class<S> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfiguration) {
		this(null, serviceConnectorType, serviceConnectorConfiguration);
	}

	/**
	 * Set the cloud, for internal testing purpose only.
	 * 
	 * <p>
	 * For normal usage, the {@link InitializingBean} approach will create (if needed) a {@link CloudFactory} and obtain a {@link Cloud} from it.
	 * 
	 * @param cloud
	 */
	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) getBeanFactory();
		
		if (cloud == null) {
			if(beanFactory.getBeansOfType(CloudFactory.class).isEmpty()) {
				beanFactory.registerSingleton(CLOUD_FACTORY_BEAN_NAME, new CloudFactory());
			}
			CloudFactory cloudFactory = beanFactory.getBeansOfType(CloudFactory.class).values().iterator().next();
			cloud = cloudFactory.getCloud();
		}
		if (!StringUtils.hasText(serviceId)) {
			List<? extends ServiceInfo> infos = cloud.getServiceInfos(serviceConnectorType);
			if (infos.size() != 1) {
				throw new CloudException("Expected 1 service matching " + serviceConnectorType.getName() + " type, but found " + infos.size());
			}
			serviceId = infos.get(0).getId(); 
		}
		
		super.afterPropertiesSet();
	}
	
	@Override
	protected S createInstance() throws Exception {
		return createService();
	}
	
	public S createService() {
	    if (serviceInstance == null && cloud != null) {
	        serviceInstance = cloud.getServiceConnector(serviceId, serviceConnectorType, serviceConnectorConfiguration);
	    }
	    return serviceInstance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<?> getObjectType() {
		if (serviceConnectorType == null) {
			try {
				serviceConnectorType = (Class<? extends S>) createService().getClass();
			}
			catch (Exception e) {
				return null;
			}
		}
		return serviceConnectorType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceConnectorType(Class<? extends S> serviceConnectorType) {
		if (serviceConnectorType != null) {
			this.serviceConnectorType = serviceConnectorType;
		}
	}
}
