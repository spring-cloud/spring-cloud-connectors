package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.relational.CloudDataSourceFactory;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;

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
	private static final String ELEMENT_DATASOURCE_NAMES = "pool-data-sources";
	private static final String ELEMENT_CONNECTION_PROPERTIES = "connection-properties";

	public CloudDataSourceFactoryParser() {
		super(CloudDataSourceFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		BeanDefinitionBuilder dataSourceConfigBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition(DataSourceConfig.class.getName());

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

		List<?> dataSourceNames = null;
		Element dataSourceNamesElement = DomUtils.getChildElementByTagName(element, ELEMENT_DATASOURCE_NAMES);
		if (dataSourceNamesElement != null) {
			dataSourceNames = parserContext.getDelegate().
					parseListElement(dataSourceNamesElement, dataSourceConfigBeanBuilder.getRawBeanDefinition());
		}

		Map<?, ?> properties = null;
		Element propertiesElement = DomUtils.getChildElementByTagName(element, ELEMENT_CONNECTION_PROPERTIES);
		if (propertiesElement != null) {
			properties = parserContext.getDelegate().parseMapElement(propertiesElement, builder.getRawBeanDefinition());
		}

		dataSourceConfigBeanBuilder.addConstructorArgValue(cloudPoolConfiguration);
		dataSourceConfigBeanBuilder.addConstructorArgValue(cloudConnectionConfiguration);
		dataSourceConfigBeanBuilder.addConstructorArgValue(dataSourceNames);
		dataSourceConfigBeanBuilder.addConstructorArgValue(properties);

		builder.addConstructorArgValue(dataSourceConfigBeanBuilder.getBeanDefinition());
	}

	private BeanDefinition parseConnectionElement(Element element) {
		BeanDefinitionBuilder cloudConnectionConfigurationBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition(DataSourceConfig.ConnectionConfig.class.getName());
		String connectionProperties = element.getAttribute("properties");
		if (StringUtils.hasText(connectionProperties)) {
			cloudConnectionConfigurationBeanBuilder.addConstructorArgValue(connectionProperties);
		}
		return cloudConnectionConfigurationBeanBuilder.getBeanDefinition();
	}
}
