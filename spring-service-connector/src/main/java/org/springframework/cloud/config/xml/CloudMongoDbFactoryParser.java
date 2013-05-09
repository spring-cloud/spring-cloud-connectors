package org.springframework.cloud.config.xml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.document.MongoDbFactoryFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for the <cloud:mongo-db-factory> namespace element
 *
 * @author Thomas Risberg
 * @author Ramnivas Laddad
 */
public class CloudMongoDbFactoryParser extends AbstractNestedElementCloudServiceFactoryParser {

	private static final String WRITE_CONCERN = "write-concern";
	private static final String MAX_WAIT_TIME = "max-wait-time";
	private static final String CONNECTIONS_PER_HOST = "connections-per-host";
	private static final String ELEMENT_MONGO_OPTIONS = "mongo-options";

	public CloudMongoDbFactoryParser() {
		super(MongoDbFactoryFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);
		
		Map<String, String> attributeMap = new HashMap<String, String>();
		parseWriteConcern(element, attributeMap);
		parseMongoOptionsElement(element, parserContext, attributeMap);

		BeanDefinitionBuilder cloudMongoConfigurationBeanBuilder =
				BeanDefinitionBuilder.genericBeanDefinition("org.springframework.cloud.service.document.MongoDbFactoryConfig");
		for (String key : new String[]{WRITE_CONCERN, CONNECTIONS_PER_HOST, MAX_WAIT_TIME}) {
			String value = attributeMap.get(key);
			//if (value != null) {
				cloudMongoConfigurationBeanBuilder.addConstructorArgValue(value);		
			//}
		}
		
		builder.addConstructorArgValue(cloudMongoConfigurationBeanBuilder.getBeanDefinition());
	}

	private void parseWriteConcern(Element element, Map<String, String> attributeMap) {
		String writeConcern = element.getAttribute(WRITE_CONCERN);
		if (StringUtils.hasText(writeConcern)) {
			attributeMap.put(WRITE_CONCERN, writeConcern);
		}
	}

	private void parseMongoOptionsElement(Element element, ParserContext parserContext, Map<String, String> attributeMap) {
		NodeList childNodes = element.getChildNodes();
		
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (isElement(child, parserContext, ELEMENT_MONGO_OPTIONS)) {
				Element optionElement = (Element) child;
				String connectionsPerHost = optionElement.getAttribute(CONNECTIONS_PER_HOST);
				if (StringUtils.hasText(connectionsPerHost)) {
					attributeMap.put(CONNECTIONS_PER_HOST, connectionsPerHost);
				}
				String maxWaitTime = optionElement.getAttribute(MAX_WAIT_TIME);
				if (StringUtils.hasText(maxWaitTime)) {
					attributeMap.put(MAX_WAIT_TIME, maxWaitTime);
				}
			}
		}
	}
}