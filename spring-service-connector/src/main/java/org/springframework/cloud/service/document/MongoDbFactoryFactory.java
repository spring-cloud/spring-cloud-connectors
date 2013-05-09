package org.springframework.cloud.service.document;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Spring factory bean for MongoDb service.
 *
 * @author Ramnivas Laddad
 *
 */
public class MongoDbFactoryFactory extends AbstractCloudServiceConnectorFactory<MongoDbFactory> {
	public MongoDbFactoryFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, MongoDbFactory.class, serviceConnectorConfiguration);
	}
}
