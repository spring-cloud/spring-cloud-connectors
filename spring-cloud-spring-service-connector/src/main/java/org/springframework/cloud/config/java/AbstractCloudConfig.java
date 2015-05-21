package org.springframework.cloud.config.java;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JavaConfig base class for simplified access to the bound services.
 *
 * Note that this class doesn't directly expose low-level methods such as getServiceInfo()
 * to keep focus on typical needs of a Spring application. But, since it exposes the cloud object itself,
 * you can always call methods on it to get that kind of information.
 *
 * @author Ramnivas Laddad
 *
 */
@Configuration
public abstract class AbstractCloudConfig implements BeanFactoryAware {

	private static final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";

	private CloudFactory cloudFactory;

	private Cloud cloud;

	private ServiceConnectionFactory connectionFactory;

	/**
	 * Get the cloud factory.
	 *
	 * Most applications will never need this method, but provided here to cover corner cases.
	 *
	 * @return cloud factory
	 */
	protected CloudFactory cloudFactory() {
		return cloudFactory;
	}

	/**
	 * Get the underlying cloud object.
	 *
	 * @return the cloud object appropriate for the current environment
	 */
	@Bean
	public Cloud cloud() {
		return cloud;
	}

	public ServiceConnectionFactory connectionFactory() {
		return connectionFactory;
	}

	/**
	 * Get the object containing service and app properties
	 *
	 * @return the properties of the discovered runtime environment
	 */
	public Properties properties() {
		return cloud().getCloudProperties();
	}

	/**
	 * Implementation note: This roundabout way of implementation is required to ensure that
	 * a {@link CloudFactory} bean if created in some other configuration is available, we should use
	 * that.
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (cloudFactory == null) {
			try {
				cloudFactory = beanFactory.getBean(CloudFactory.class);
			} catch (NoSuchBeanDefinitionException ex) {
				cloudFactory = new CloudFactory();
				((SingletonBeanRegistry) beanFactory).registerSingleton(CLOUD_FACTORY_BEAN_NAME, cloudFactory);
			}
		}
		this.cloud = cloudFactory.getCloud();
		this.connectionFactory = new CloudServiceConnectionFactory(cloud);
	}

}
