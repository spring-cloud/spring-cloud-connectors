package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Parser for the {@code <cloud:rabbit-connection-factory>} namespace element
 *
 * @author Thomas Risberg
 * @author Ramnivas Laddad
 * @author Scott Frederick
 */
public class CloudRabbitConnectionFactoryParser extends AbstractNestedElementCloudServiceFactoryParser {

	private static final String RABBIT_OPTIONS = "rabbit-options";
	private static final String CONNECTION_PROPERTIES = "connection-properties";
	private static final String CHANNEL_CACHE_SIZE = "channel-cache-size";

	public CloudRabbitConnectionFactoryParser() {
		super(RabbitConnectionFactoryFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		BeanDefinition cloudRabbitConfiguration = parseRabbitOptionsElement(element, parserContext);

		builder.addConstructorArgValue(cloudRabbitConfiguration);
	}

	private BeanDefinition parseRabbitOptionsElement(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder configBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition(RabbitConnectionFactoryConfig.class.getName());

		Element propertiesElement = DomUtils.getChildElementByTagName(element, CONNECTION_PROPERTIES);
		if (propertiesElement != null) {
			Map<?, ?> properties = parserContext.getDelegate().parseMapElement(propertiesElement, configBeanBuilder.getRawBeanDefinition());
			configBeanBuilder.addConstructorArgValue(properties);
		}

		Element rabbitOptionsElement = DomUtils.getChildElementByTagName(element, RABBIT_OPTIONS);
		if (rabbitOptionsElement != null) {
			String channelCacheSize = rabbitOptionsElement.getAttribute(CHANNEL_CACHE_SIZE);
			configBeanBuilder.addConstructorArgValue(channelCacheSize);
		}

		if (propertiesElement == null && rabbitOptionsElement == null) {
			return null;
		}
		return configBeanBuilder.getBeanDefinition();
	}
}
