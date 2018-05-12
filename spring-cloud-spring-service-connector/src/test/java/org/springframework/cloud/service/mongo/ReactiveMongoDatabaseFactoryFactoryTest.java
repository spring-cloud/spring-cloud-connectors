package org.springframework.cloud.service.mongo;

import org.mockito.Mock;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.cloud.service.document.ReactiveMongoDatabaseFactoryFactory;
import org.springframework.cloud.util.UriInfo;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;

/**
 * @author Mark Paluch
 */
public class ReactiveMongoDatabaseFactoryFactoryTest extends AbstractCloudServiceConnectorFactoryTest<ReactiveMongoDatabaseFactoryFactory, ReactiveMongoDatabaseFactory, MongoServiceInfo> {
	@Mock ReactiveMongoDatabaseFactory mockConnector;
	
	public ReactiveMongoDatabaseFactoryFactory createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config) {
		return new ReactiveMongoDatabaseFactoryFactory(id, config);
	}
	
	public Class<ReactiveMongoDatabaseFactory> getConnectorType() {
		return ReactiveMongoDatabaseFactory.class;
	}
	
	public ReactiveMongoDatabaseFactory getMockConnector() {
		return mockConnector;
	}
	
	public MongoServiceInfo getTestServiceInfo(String id) {
		return new MongoServiceInfo(id, new UriInfo("mongodb", "host", 0, "username", "password", "db").getUriString());
	}
}
