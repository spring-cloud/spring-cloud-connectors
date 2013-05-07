package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.GenericCloudServiceConnectorFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;


/**
 * Support for the catch-all <cloud:service> namespace
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericCloudServiceFactoryParser extends AbstractCloudServiceFactoryParser {

	private static final String CONNECTOR_TYPE = "connector-type";
	private Class<?> connectorType = null;

	public GenericCloudServiceFactoryParser() {
		super(GenericCloudServiceConnectorFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);
		
		String connectorTypeName = element.getAttribute(CONNECTOR_TYPE);
		if (StringUtils.hasText(connectorTypeName)) {
			try {
				connectorType = Class.forName(connectorTypeName);
				builder.addPropertyValue("serviceConnectorType", connectorType);
			} catch (ClassNotFoundException ex) {
				throw new CloudException("Failed to load " + connectorTypeName, ex);
			}
		}
		
		// TBD: Support generic (map-based?) service config
		builder.addConstructorArgValue(null);
	}
}
