package org.springframework.cloud.service.mongo;

import org.mockito.Mock;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.document.MongoDbFactoryFactory;
import org.springframework.cloud.service.document.MongoServiceInfo;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoDbFactoryFactoryTest extends AbstractCloudServiceConnectorFactoryTest<MongoDbFactoryFactory, MongoDbFactory, MongoServiceInfo> {
	@Mock MongoDbFactory mockConnector;
	
	public MongoDbFactoryFactory createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config) {
		return new MongoDbFactoryFactory(id, config);
	}
	
	public Class<MongoDbFactory> getConnectorType() {
		return MongoDbFactory.class;
	}
	
	public MongoDbFactory getMockConnector() {
		return mockConnector;
	}
	
	public MongoServiceInfo getTestServiceInfo(String id) {
		return new MongoServiceInfo(id, "host", 0, "username", "password", "db");
	}
}
