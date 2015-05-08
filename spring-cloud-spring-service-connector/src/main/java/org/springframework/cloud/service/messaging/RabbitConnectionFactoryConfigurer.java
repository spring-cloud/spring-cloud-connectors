package org.springframework.cloud.service.messaging;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.cloud.service.MapServiceConnectionConfigurer;
import org.springframework.cloud.service.MapServiceConnectorConfig;

/**
 * @author Scott Frederick
 */
public class RabbitConnectionFactoryConfigurer extends MapServiceConnectionConfigurer<ConnectionFactory, MapServiceConnectorConfig> {
}
