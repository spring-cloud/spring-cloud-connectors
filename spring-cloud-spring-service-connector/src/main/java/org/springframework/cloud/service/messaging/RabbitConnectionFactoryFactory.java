package org.springframework.cloud.service.messaging;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Spring factory bean for RabbitMQ service.
 *
 * @author Ramnivas Laddad
 *
 */
public class RabbitConnectionFactoryFactory extends AbstractCloudServiceConnectorFactory<ConnectionFactory> {
	public RabbitConnectionFactoryFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, ConnectionFactory.class, serviceConnectorConfiguration);
	}
}
