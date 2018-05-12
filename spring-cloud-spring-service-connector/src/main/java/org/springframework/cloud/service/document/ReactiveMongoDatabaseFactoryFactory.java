package org.springframework.cloud.service.document;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;

/**
 * Spring factory bean for reactive MongoDb service.
 *
 * @author Mark Paluch
 */
public class ReactiveMongoDatabaseFactoryFactory extends AbstractCloudServiceConnectorFactory<ReactiveMongoDatabaseFactory> {
	public ReactiveMongoDatabaseFactoryFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, ReactiveMongoDatabaseFactory.class, serviceConnectorConfiguration);
	}
}
