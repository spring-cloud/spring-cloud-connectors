package org.springframework.cloud.service.messaging;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.cloud.service.MapServiceConnectionConfigurer;
import org.springframework.cloud.service.MapServiceConnectorConfig;

/**
 * @author Scott Frederick
 */
public class SpringConnectionFactoryConfigurer extends MapServiceConnectionConfigurer<CachingConnectionFactory, MapServiceConnectorConfig> {
}
