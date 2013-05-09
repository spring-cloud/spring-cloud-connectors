package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.keyval.RedisConnectionFactoryFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for the <cloud:redis-connection-factory> namespace element
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudRedisConnectionFactoryParser extends AbstractPoolingCloudServiceFactoryParser {

	public CloudRedisConnectionFactoryParser() {
		super(RedisConnectionFactoryFactory.class);
	}

	private static final String ELEMENT_POOL = "pool";

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);
		BeanDefinition cloudPoolConfiguration = null;
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (isElement(child, parserContext, ELEMENT_POOL)) {
				cloudPoolConfiguration = parsePoolElement((Element) child, parserContext);
			}
		}
		
		BeanDefinitionBuilder redisConfigBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.PooledServiceConnectorConfig");
		redisConfigBeanBuilder.addConstructorArgValue(cloudPoolConfiguration);

		builder.addConstructorArgValue(redisConfigBeanBuilder.getBeanDefinition());
	}	
}