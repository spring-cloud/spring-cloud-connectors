package org.springframework.cloud.config.xml;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.CloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Common class to support &lt;cloud&gt; namespace.
 * 
 * @author Ramnivas Laddad
 * 
 * Implementation notes:
 * This parser adds a {@link CloudServiceIntroducer} bean supplying it the {@link CloudServiceConnectorFactory} 
 * class that was passed as the constructor parameter. The added {@link CloudServiceIntroducer} bean introduces 
 * the service connector factory, which in turn creates the required service connector.
 * This level of indirection allows attaching a proper 'id' to the service connector object. 
 */
class AbstractCloudServiceFactoryParser extends AbstractSingleBeanDefinitionParser {
	private final Class<? extends CloudServiceConnectorFactory<?>> serviceConnectorFactoryType;

	public AbstractCloudServiceFactoryParser(Class<? extends CloudServiceConnectorFactory<?>> serviceConnectorFactoryType) {
		Assert.notNull(serviceConnectorFactoryType, "serviceConnectorFactoryType must not be null");
		this.serviceConnectorFactoryType = serviceConnectorFactoryType;
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}
	
	@Override
	protected final Class<?> getBeanClass(Element element) {
		return CloudServiceIntroducer.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String serviceId = element.getAttribute("service-name");
		builder.addConstructorArgValue(serviceConnectorFactoryType);
		builder.addConstructorArgValue(element.getAttribute(ID_ATTRIBUTE));
		builder.addConstructorArgValue(serviceId);
		// subclasses should add one more constructor parameter for ServiceConnectorConfig
	}
}

class CloudServiceIntroducer implements BeanFactoryPostProcessor {
	private Class<? extends CloudServiceConnectorFactory<?>> serviceConnectorFactoryType;
	private String beanId;
	private String serviceId;
	private ServiceConnectorConfig serviceConnectorConfig;
	
	private Class<?> serviceConnectorType;

	public CloudServiceIntroducer(Class<? extends CloudServiceConnectorFactory<?>> serviceConnectorFactoryType, 
								  String beanId, String serviceId, ServiceConnectorConfig serviceConnectorConfig) {
		this.serviceConnectorFactoryType = serviceConnectorFactoryType;
		this.beanId = beanId;
		this.serviceId = serviceId;
		this.serviceConnectorConfig = serviceConnectorConfig;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			Constructor<?> ctor = serviceConnectorFactoryType.getConstructor(String.class, ServiceConnectorConfig.class);
			AbstractCloudServiceConnectorFactory<?> serviceFactory = (AbstractCloudServiceConnectorFactory<?>)ctor.newInstance(serviceId, serviceConnectorConfig);
			serviceFactory.setServiceConnectorType((Class)serviceConnectorType);
			serviceFactory.setBeanFactory(beanFactory);
			serviceFactory.afterPropertiesSet();
			// id is the beanId if specified, otherwise the serviceId
			if (StringUtils.hasText(beanId)) {
				beanFactory.registerSingleton(beanId, serviceFactory);
			} else {
				beanFactory.registerSingleton(serviceFactory.getServiceId(), serviceFactory);
			}
		} catch (Exception ex) {
			throw new BeanCreationException("Error registering service factory", ex);
		}
	}
	
	/**
	 * To set connector type that is more specific that the factory is meant to produce.
	 * 
	 * @param serviceConnectorType
	 */
	public void setServiceConnectorType(Class<?> serviceConnectorType) {
		this.serviceConnectorType = serviceConnectorType;
	}

}