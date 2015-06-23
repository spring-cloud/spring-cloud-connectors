package org.springframework.cloud.config.xml;

import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.policies.RetryPolicy;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cloud.service.column.CassandraConnectorConfig;
import org.springframework.cloud.service.column.CassandraSessionFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Vinicius Carvalho
 */
public class CloudCassandraSessionParser extends AbstractNestedElementCloudServiceFactoryParser {

    final String ELEMENT_CASSANDRA_OPTIONS = "cassandra-options";
    final String COMPRESSION_ATTRIBUTE = "compression";
    final String RETRY_POLICY_ATTRIBUTE = "retry-policy";
    final String LOAD_BALANCING_POLICY_ATTRIBUTE = "loadbalancing-policy";
    final String SOCKET_OPTIONS_ATTRIBUTE = "socket-options";
    final String RECONNECTION_POLICY_ATTRIBUTE ="reconnection-policy";

    public CloudCassandraSessionParser() {
        super(CassandraSessionFactory.class);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        Element optionsElement = DomUtils.getChildElementByTagName(element, ELEMENT_CASSANDRA_OPTIONS);

        BeanDefinitionBuilder beanBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(CassandraConnectorConfig.class.getName());

        if(optionsElement != null) {
            String compressionString = optionsElement.getAttribute(COMPRESSION_ATTRIBUTE);
            if (!StringUtils.isEmpty(compressionString)) {
                ProtocolOptions.Compression compression = ProtocolOptions.Compression.valueOf(compressionString);
                beanBuilder.addPropertyValue("compression", compression);
            }

            String retryPolicyString = optionsElement.getAttribute(RETRY_POLICY_ATTRIBUTE);
            if (!StringUtils.isEmpty(retryPolicyString)) {
                beanBuilder.addPropertyReference("retryPolicy", retryPolicyString);
            }

            String loadBalancingPolicyString = optionsElement.getAttribute(LOAD_BALANCING_POLICY_ATTRIBUTE);
            if (!StringUtils.isEmpty(loadBalancingPolicyString)) {
                beanBuilder.addPropertyReference("loadBalancingPolicy", loadBalancingPolicyString);
            }

            String socketOptionsString = optionsElement.getAttribute(SOCKET_OPTIONS_ATTRIBUTE);
            if(!StringUtils.isEmpty(socketOptionsString)){
                beanBuilder.addPropertyReference("socketOptions",socketOptionsString);
            }

            String reconnectionPolicyString = optionsElement.getAttribute(RECONNECTION_POLICY_ATTRIBUTE);
            if(!StringUtils.isEmpty(reconnectionPolicyString)){
                beanBuilder.addPropertyReference("reconnectionPolicy",reconnectionPolicyString);
            }

        }



        builder.addConstructorArgValue(beanBuilder.getBeanDefinition());
    }

}
