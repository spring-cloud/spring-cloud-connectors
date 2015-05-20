package org.springframework.cloud.service.messaging;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.AmqpServiceInfo;

import java.net.URI;
import java.net.URISyntaxException;

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
		com.rabbitmq.client.ConnectionFactory connectionFactory = createRabbitConnectionFactory(serviceInfo);

		configurer.configure(connectionFactory, (RabbitConnectionFactoryConfig) serviceConnectorConfiguration);

		return createSpringConnectionFactory(serviceInfo, serviceConnectorConfiguration, connectionFactory);
	}

	private com.rabbitmq.client.ConnectionFactory createRabbitConnectionFactory(AmqpServiceInfo serviceInfo) {
		com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
		if (serviceInfo.getUris() != null && serviceInfo.getUris().size() > 0) {
			setConnectionFactoryUri(connectionFactory, serviceInfo.getUris().get(0));
		} else {
			setConnectionFactoryUri(connectionFactory, serviceInfo.getUri());
		}
		return connectionFactory;
	}

	private void setConnectionFactoryUri(com.rabbitmq.client.ConnectionFactory connectionFactory, String uri) {
		try {
			connectionFactory.setUri(uri);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid AMQP URI", e);
		}
	}

	private CachingConnectionFactory createSpringConnectionFactory(AmqpServiceInfo serviceInfo,
																   ServiceConnectorConfig serviceConnectorConfiguration,
																   com.rabbitmq.client.ConnectionFactory connectionFactory) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);

		if (serviceInfo.getUris() != null) {
			cachingConnectionFactory.setAddresses(getAddresses(serviceInfo));
		}

		if (serviceConnectorConfiguration != null) {
			Integer channelCacheSize = ((RabbitConnectionFactoryConfig) serviceConnectorConfiguration).getChannelCacheSize();
			if (channelCacheSize != null) {
				cachingConnectionFactory.setChannelCacheSize(channelCacheSize);
			}
		}

		return cachingConnectionFactory;
	}

	private String getAddresses(AmqpServiceInfo serviceInfo) {
		try {
			StringBuilder addresses = new StringBuilder();
			for (String uriString : serviceInfo.getUris()) {
				URI uri = new URI(uriString);
				if (addresses.length() > 0) {
					addresses.append(',');
				}
				addresses.append(uri.getHost()).append(':').append(uri.getPort());
			}
			return addresses.toString();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid AMQP URI", e);
		}
	}
}
