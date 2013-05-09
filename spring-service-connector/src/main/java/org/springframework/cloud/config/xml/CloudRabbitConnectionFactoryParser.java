package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for the <cloud:rabbit-connection-factory> namespace element
 *
 * @author Thomas Risberg
 * @author Ramnivas Laddad
 */
public class CloudRabbitConnectionFactoryParser extends AbstractNestedElementCloudServiceFactoryParser {

	private static final String ELEMENT_RABBIT_OPTIONS = "rabbit-options";

	public CloudRabbitConnectionFactoryParser() {
		super(RabbitConnectionFactoryFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);
		BeanDefinition cloudRabbitConfiguration = null;
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (isElement(child, parserContext, ELEMENT_RABBIT_OPTIONS)) {
				cloudRabbitConfiguration = parseRabbitOptionsElement((Element) child);
			}
		}

		builder.addConstructorArgValue(cloudRabbitConfiguration);
	}

	private BeanDefinition parseRabbitOptionsElement(Element element) {
		BeanDefinitionBuilder cloudRabbitConfigurationBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig");
		String channelCacheSize = element.getAttribute("channel-cache-size");

		cloudRabbitConfigurationBeanBuilder.addConstructorArgValue(channelCacheSize);

		return cloudRabbitConfigurationBeanBuilder.getBeanDefinition();
	}
}
