package org.springframework.cloud.service.redis;

import org.mockito.Mock;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.keyval.RedisConnectionFactoryFactory;
import org.springframework.cloud.service.keyval.RedisServiceInfo;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RedisConnectionFactoryFactoryTest extends AbstractCloudServiceConnectorFactoryTest<RedisConnectionFactoryFactory, RedisConnectionFactory, RedisServiceInfo> {
	@Mock RedisConnectionFactory mockConnector;
	
	public RedisConnectionFactoryFactory createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config) {
		return new RedisConnectionFactoryFactory(id, config);
	}
	
	public Class<RedisConnectionFactory> getConnectorType() {
		return RedisConnectionFactory.class;
	}
	
	public RedisConnectionFactory getMockConnector() {
		return mockConnector;
	}
	
	public RedisServiceInfo getTestServiceInfo(String id) {
		return new RedisServiceInfo(id, "host", 0, "password");
	}
}
