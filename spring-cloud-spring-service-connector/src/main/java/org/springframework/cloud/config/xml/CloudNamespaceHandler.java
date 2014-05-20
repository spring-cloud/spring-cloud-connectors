package org.springframework.cloud.config.xml;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.cloud.config.ServiceScanHelper;
import org.w3c.dom.Element;

/**
 * Handler for the 'cloud' namespace
 *
 * @author Ramnivas Laddad
 *
 */
public class CloudNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("service", new GenericCloudServiceFactoryParser());

		registerBeanDefinitionParser("rabbit-connection-factory", new CloudRabbitConnectionFactoryParser());
		registerBeanDefinitionParser("redis-connection-factory", new CloudRedisConnectionFactoryParser());
		registerBeanDefinitionParser("mongo-db-factory", new CloudMongoDbFactoryParser());
		registerBeanDefinitionParser("data-source", new CloudDataSourceFactoryParser());

		this.registerBeanDefinitionParser("properties", new AbstractSimpleBeanDefinitionParser() {
			@Override
			protected Class<?> getBeanClass(Element element) {
				return CloudPropertiesFactoryBean.class;
			}
		});
		
		this.registerBeanDefinitionParser("service-scan", new AbstractSimpleBeanDefinitionParser() {
			@Override
			protected Class<?> getBeanClass(Element element) {
				return ServiceScanBeanFactoryProcessor.class;
			}

			@Override
			protected boolean shouldGenerateId() {
				return true;
			}
		});
	}
	
	public static class ServiceScanBeanFactoryProcessor implements BeanFactoryPostProcessor {
	    private ServiceScanHelper helper = new ServiceScanHelper();
	    
	    @Override
	    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	        helper.registerServiceBeans((BeanDefinitionRegistry) beanFactory);
	    }
	    
	}
	
}

