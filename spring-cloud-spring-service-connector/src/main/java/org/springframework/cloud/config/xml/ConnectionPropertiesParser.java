package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * @author Scott Frederick
 */
class ConnectionPropertiesParser extends AbstractSingleBeanDefinitionParser {
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		Map<?, ?> map = parserContext.getDelegate().parseMapElement(element, builder.getRawBeanDefinition());
		builder.addPropertyValue("sourceMap", map);
	}

	@Override
	protected String getBeanClassName(Element element) {
		return MapFactoryBean.class.getName();
	}
}