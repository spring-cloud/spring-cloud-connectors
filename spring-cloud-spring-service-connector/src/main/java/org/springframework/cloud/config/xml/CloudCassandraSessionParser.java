/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.column.CassandraClusterConfig;
import org.springframework.cloud.service.column.CassandraClusterFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.datastax.driver.core.ProtocolOptions;

/**
 * @author Vinicius Carvalho
 */
public class CloudCassandraSessionParser
		extends AbstractNestedElementCloudServiceFactoryParser {

	final String ELEMENT_CASSANDRA_OPTIONS = "cassandra-options";
	final String COMPRESSION_ATTRIBUTE = "compression";
	final String RETRY_POLICY_ATTRIBUTE = "retry-policy";
	final String LOAD_BALANCING_POLICY_ATTRIBUTE = "loadbalancing-policy";
	final String SOCKET_OPTIONS_ATTRIBUTE = "socket-options";
	final String RECONNECTION_POLICY_ATTRIBUTE = "reconnection-policy";

	public CloudCassandraSessionParser() {
		super(CassandraClusterFactory.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		Element optionsElement = DomUtils.getChildElementByTagName(element,
				ELEMENT_CASSANDRA_OPTIONS);

		BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(CassandraClusterConfig.class.getName());

		if (optionsElement != null) {
			String compressionString = optionsElement.getAttribute(COMPRESSION_ATTRIBUTE);
			if (!StringUtils.isEmpty(compressionString)) {
				ProtocolOptions.Compression compression = ProtocolOptions.Compression
						.valueOf(compressionString);
				beanBuilder.addPropertyValue("compression", compression);
			}

			String retryPolicyString = optionsElement
					.getAttribute(RETRY_POLICY_ATTRIBUTE);
			if (!StringUtils.isEmpty(retryPolicyString)) {
				beanBuilder.addPropertyReference("retryPolicy", retryPolicyString);
			}

			String loadBalancingPolicyString = optionsElement
					.getAttribute(LOAD_BALANCING_POLICY_ATTRIBUTE);
			if (!StringUtils.isEmpty(loadBalancingPolicyString)) {
				beanBuilder.addPropertyReference("loadBalancingPolicy",
						loadBalancingPolicyString);
			}

			String socketOptionsString = optionsElement
					.getAttribute(SOCKET_OPTIONS_ATTRIBUTE);
			if (!StringUtils.isEmpty(socketOptionsString)) {
				beanBuilder.addPropertyReference("socketOptions", socketOptionsString);
			}

			String reconnectionPolicyString = optionsElement
					.getAttribute(RECONNECTION_POLICY_ATTRIBUTE);
			if (!StringUtils.isEmpty(reconnectionPolicyString)) {
				beanBuilder.addPropertyReference("reconnectionPolicy",
						reconnectionPolicyString);
			}

		}

		builder.addConstructorArgValue(beanBuilder.getBeanDefinition());
	}

}