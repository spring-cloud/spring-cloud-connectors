package org.springframework.cloud.service.messaging;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Simplified access to creating RabbitMQ service objects.
 * 
 * @author Ramnivas Laddad
 * @author Dave Syer
 * @author Thomas Risberg
 * 
 */

public class RabbitConnectionFactoryCreator extends	AbstractServiceConnectorCreator<ConnectionFactory, RabbitServiceInfo> {
	@Override
	public ConnectionFactory create(RabbitServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfiguration) {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(serviceInfo.getHost());
		connectionFactory.setVirtualHost(serviceInfo.getVirtualHost());
		connectionFactory.setUsername(serviceInfo.getUserName());
		connectionFactory.setPassword(serviceInfo.getPassword());
		connectionFactory.setPort(serviceInfo.getPort());
		
		if (serviceConnectorConfiguration != null) {
			connectionFactory.setChannelCacheSize(((RabbitConnectionFactoryConfig)serviceConnectorConfiguration).getChannelCacheSize());
		}
		
		return connectionFactory;
	}
}
