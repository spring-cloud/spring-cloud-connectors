package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.CloudServiceConnectorFactory;
import org.w3c.dom.Node;

/**
 * @author Thomas Risberg
 */
abstract class AbstractNestedElementCloudServiceFactoryParser extends AbstractCloudServiceFactoryParser {

	public AbstractNestedElementCloudServiceFactoryParser(Class<? extends CloudServiceConnectorFactory<?>> serviceConnectorFactoryType) {
		super(serviceConnectorFactoryType);
	}

	protected boolean isElement(Node node, ParserContext parserContext, String elementName) {
		return node.getNodeType() == Node.ELEMENT_NODE &&
				elementName.equals(parserContext.getDelegate().getLocalName(node));
	}
}
