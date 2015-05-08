package org.springframework.cloud.service.messaging;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 * Simplified access to creating RabbitMQ service objects.
 * 
 * @author Ramnivas Laddad
 * @author Dave Syer
 * @author Thomas Risberg
 * @author Mark Fisher
 * @author Scott Frederick
 */
public class RabbitConnectionFactoryCreator extends	AbstractServiceConnectorCreator<ConnectionFactory, AmqpServiceInfo> {
	private RabbitConnectionFactoryConfigurer configurer = new RabbitConnectionFactoryConfigurer();

	@Override
	public ConnectionFactory create(AmqpServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfiguration) {
		com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
		try {
			connectionFactory.setUri(serviceInfo.getUri());
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to create ConnectionFactory", e);
		}
		configurer.configure(connectionFactory, (RabbitConnectionFactoryConfig) serviceConnectorConfiguration);

		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);

		if (serviceConnectorConfiguration != null) {
			Integer channelCacheSize = ((RabbitConnectionFactoryConfig) serviceConnectorConfiguration).getChannelCacheSize();
			if (channelCacheSize != null) {
				cachingConnectionFactory.setChannelCacheSize(channelCacheSize);
			}
		}

		return cachingConnectionFactory;
	}

}
