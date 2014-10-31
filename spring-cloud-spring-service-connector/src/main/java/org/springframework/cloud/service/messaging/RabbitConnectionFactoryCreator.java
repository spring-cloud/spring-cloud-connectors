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
 */
public class RabbitConnectionFactoryCreator extends	AbstractServiceConnectorCreator<ConnectionFactory, AmqpServiceInfo> {

	@Override
	public ConnectionFactory create(AmqpServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfiguration) {
		com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
		connectionFactory.setHost(serviceInfo.getHost());
		connectionFactory.setVirtualHost(serviceInfo.getVirtualHost());
		connectionFactory.setUsername(serviceInfo.getUserName());
		connectionFactory.setPassword(serviceInfo.getPassword());
		if ("amqps".equals(serviceInfo.getScheme())) {		
			try {
				connectionFactory.useSslProtocol();
			}
			catch (Exception e) {
				throw new IllegalStateException("failed to configure SSL protocol", e);
			}
		}
		connectionFactory.setPort(serviceInfo.getPort());
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
		if (serviceConnectorConfiguration != null) {
			cachingConnectionFactory.setChannelCacheSize(((RabbitConnectionFactoryConfig)serviceConnectorConfiguration).getChannelCacheSize());
		}
		return cachingConnectionFactory;
	}

}
