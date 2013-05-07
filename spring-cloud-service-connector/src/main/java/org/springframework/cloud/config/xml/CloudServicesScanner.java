package org.springframework.cloud.config.xml;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.GenericCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceInfo;

/**
 * Bean factory post processor that adds a bean for each service bound to the application.
 *
 * <p>
 * Each service populated by this bean has the same name as the service it is bound to.
 * </p>
 *
 * Usage:
 * <p>
 * An application may add a bean of this type:
 * <pre>
 * &lt;bean class="org.cloudfoundry.runtime.service.CloudServicesScanner"/&gt;
 * </pre>
 * to have an easy access to all the services.
 *
 * If there is unique bean of a type, you can inject beans using the following
 * code (shows Redis, but the same scheme works for all services):
 * <pre>
 * &#64;Autowired RedisConnectionFactory redisConnectionFactory;
 * </pre>
 *
 * If there are more than one services of a type, you can use the @Qualifier
 * as in the following code:
 * <pre>
 * &#64;Autowired &#64;Qualifier("service-name1") RedisConnectionFactory redisConnectionFactory;
 * &#64;Autowired &#64;Qualifier("service-name2") RedisConnectionFactory redisConnectionFactory;
 * </pre>
 *
 * You may, of course, use XML-based configuration.
 *
 * @author Ramnivas Laddad
 * @author Jennifer Hickey
 *
 */
public class CloudServicesScanner implements BeanFactoryPostProcessor, InitializingBean, BeanFactoryAware {
	private static final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";
	
	private BeanFactory beanFactory = null;
	private Cloud cloud;


	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) this.beanFactory;
		
		if (cloud == null) {
			if(beanFactory.getBeansOfType(CloudFactory.class).isEmpty()) {
				beanFactory.registerSingleton(CLOUD_FACTORY_BEAN_NAME, new CloudFactory());
			}
			CloudFactory cloudFactory = beanFactory.getBeansOfType(CloudFactory.class).values().iterator().next();
			cloud = cloudFactory.getCloud();
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		registerServiceBeans(cloud, beanFactory);
	}

	private void registerServiceBeans(Cloud cloud, ConfigurableListableBeanFactory beanFactory) {
		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
		
		for(ServiceInfo serviceInfo: serviceInfos) {
			registerServiceBean(beanFactory, serviceInfo);
		}
	}
	
	private void registerServiceBean(ConfigurableListableBeanFactory beanFactory, ServiceInfo serviceInfo) {
		try {
			AbstractCloudServiceConnectorFactory<?> serviceFactory = new GenericCloudServiceConnectorFactory(serviceInfo.getId(), null);
			serviceFactory.setBeanFactory(beanFactory);
			serviceFactory.afterPropertiesSet();
			beanFactory.registerSingleton(serviceInfo.getId(), serviceFactory);
		} catch (Exception ex) {
			throw new CloudException("Error registering service factory", ex);
		}

	}
}
