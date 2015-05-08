package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.relational.CloudDataSourceFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parser for the {@code <cloud:data-source>} namespace element
 *
 * @author Ramnivas Laddad
 * @author Thomas Risberg
 * @author Scott Frederick
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
		Element connectionElement = DomUtils.getChildElementByTagName(element, ELEMENT_CONNECTION);
		if (connectionElement != null) {
			cloudConnectionConfiguration = parseConnectionElement(connectionElement);
		}

		BeanDefinition cloudPoolConfiguration = null;
		Element poolElement = DomUtils.getChildElementByTagName(element, ELEMENT_POOL);
		if (poolElement != null) {
			cloudPoolConfiguration = parsePoolElement(poolElement, parserContext);
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
