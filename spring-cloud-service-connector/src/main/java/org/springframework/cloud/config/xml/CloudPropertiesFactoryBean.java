package org.springframework.cloud.config.xml;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;

/**
 * FactoryBean exposing cloud properties. Suitable for property placeholder
 * processing.
 *
 * @author Scott Andrews
 * @author Ramnivas Laddad
 */
public class CloudPropertiesFactoryBean implements FactoryBean<Properties>, BeanFactoryAware, InitializingBean {

	private BeanFactory beanFactory;
	private Cloud cloud;
	private final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Class<Properties> getObjectType() {
		return Properties.class;
	}

	@Override
	public Properties getObject() throws Exception {
		return cloud.getCloudProperties();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigurableListableBeanFactory listableBeanFactory = (ConfigurableListableBeanFactory) beanFactory;
		
		if (cloud == null) {
			if(listableBeanFactory.getBeansOfType(CloudFactory.class).isEmpty()) {
				listableBeanFactory.registerSingleton(CLOUD_FACTORY_BEAN_NAME , new CloudFactory());
			}
			CloudFactory cloudFactory = listableBeanFactory.getBeansOfType(CloudFactory.class).values().iterator().next();
			cloud = cloudFactory.getCloud();
		}
	}
}
