package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.CloudServiceConnectorFactory;
import org.w3c.dom.Element;


/**
 * Support for service connector that can use pooling
 * 
 * @author Ramnivas Laddad
 * @author Thomas Risberg
 */
abstract class AbstractPoolingCloudServiceFactoryParser extends AbstractNestedElementCloudServiceFactoryParser {

	public AbstractPoolingCloudServiceFactoryParser(Class<? extends CloudServiceConnectorFactory<?>> beanClass) {
		super(beanClass);
	}

	protected BeanDefinition parsePoolElement(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder cloudPoolConfigurationBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig");
		String poolSize = element.getAttribute("pool-size");
		String maxWaitTime = element.getAttribute("max-wait-time");

		cloudPoolConfigurationBeanBuilder.addConstructorArgValue(poolSize);
		cloudPoolConfigurationBeanBuilder.addConstructorArgValue(maxWaitTime);
		
		return cloudPoolConfigurationBeanBuilder.getBeanDefinition();
	}
}
