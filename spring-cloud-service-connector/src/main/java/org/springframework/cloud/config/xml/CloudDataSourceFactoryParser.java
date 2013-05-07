package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.relational.CloudDataSourceFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for the <cloud:data-source> namespace element
 *
 * @author Ramnivas Laddad
 * @author Thomas Risberg
 */
public class CloudDataSourceFactoryParser extends AbstractPoolingCloudServiceFactoryParser {

	private static final String ELEMENT_CONNECTION = "connection";
	private static final String ELEMENT_POOL = "pool";

	public CloudDataSourceFactoryParser() {
		super(CloudDataSourceFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);
		BeanDefinition cloudConnectionConfiguration = null;
		BeanDefinition cloudPoolConfiguration = null;
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (isElement(child, parserContext, ELEMENT_CONNECTION)) {
				cloudConnectionConfiguration = parseConnectionElement((Element) child);
			} else if (isElement(child, parserContext, ELEMENT_POOL)) {
				cloudPoolConfiguration = parsePoolElement((Element) child, parserContext);
			}
		}
		
		BeanDefinitionBuilder dataSourceConfigBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.relational.DataSourceConfig");
		dataSourceConfigBeanBuilder.addConstructorArgValue(cloudPoolConfiguration);
		dataSourceConfigBeanBuilder.addConstructorArgValue(cloudConnectionConfiguration);

		builder.addConstructorArgValue(dataSourceConfigBeanBuilder.getBeanDefinition());
	}

	private BeanDefinition parseConnectionElement(Element element) {
		BeanDefinitionBuilder cloudConnectionConfigurationBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig");
		String connectionProperties = element.getAttribute("properties");
		if (StringUtils.hasText(connectionProperties)) {
			cloudConnectionConfigurationBeanBuilder.addConstructorArgValue(connectionProperties);
		}
		return cloudConnectionConfigurationBeanBuilder.getBeanDefinition();
	}
}
