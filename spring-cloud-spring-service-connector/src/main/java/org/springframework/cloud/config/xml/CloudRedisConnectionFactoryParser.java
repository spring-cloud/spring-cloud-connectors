package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.keyval.RedisConnectionFactoryFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parser for the {@code <cloud:redis-connection-factory>} namespace element
 *
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class CloudRedisConnectionFactoryParser extends AbstractPoolingCloudServiceFactoryParser {

	private static final String ELEMENT_POOL = "pool";

	public CloudRedisConnectionFactoryParser() {
		super(RedisConnectionFactoryFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		BeanDefinition cloudPoolConfiguration = null;
		Element poolElement = DomUtils.getChildElementByTagName(element, ELEMENT_POOL);
		if (poolElement != null) {
			cloudPoolConfiguration = parsePoolElement(poolElement, parserContext);
		}

		BeanDefinitionBuilder redisConfigBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.PooledServiceConnectorConfig");
		redisConfigBeanBuilder.addConstructorArgValue(cloudPoolConfiguration);

		builder.addConstructorArgValue(redisConfigBeanBuilder.getBeanDefinition());
	}
}