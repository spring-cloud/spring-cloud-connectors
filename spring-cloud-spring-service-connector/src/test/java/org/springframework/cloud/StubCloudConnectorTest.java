package org.springframework.cloud;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;

/**
 * Base class for close-to-integration tests that use a stub {@link CloudConnector} to avoid the need for a real cloud environment.
 *
 * @author Ramnivas Laddad
 * @author Chris Schaefer
 */
abstract public class StubCloudConnectorTest {
	private static final String MOCK_CLOUD_BEAN_NAME = "mockCloud";

	protected ApplicationContext getTestApplicationContext(String fileName, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);
		
		return new ClassPathXmlApplicationContext(getClass().getPackage().getName().replaceAll("\\.", "/") + "/" + fileName) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.getCloudConnectors().clear();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}
	
	protected ApplicationContext getTestApplicationContext(Class<?> configClass, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);
		
		return new AnnotationConfigApplicationContext(configClass) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.getCloudConnectors().clear();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}
	
	// Helper for subclasses to use
	protected PostgresqlServiceInfo createPostgresqlService(String id) {
		return new PostgresqlServiceInfo(id, "postgres://username:pass@host:port/db");
	}
	
	protected MysqlServiceInfo createMysqlService(String id) {
		return new MysqlServiceInfo(id, "mysql://username:pass@host:port/db");
	}
	
	protected MongoServiceInfo createMongoService(String id) {
		return new MongoServiceInfo(id, "10.20.30.40", 1234, "username", "password", "db");
	}

	protected MongoServiceInfo createMongoServiceWithMultipleHostsByUri(String id) {
		return new MongoServiceInfo(id, "mongo://username:password@10.20.30.40,10.20.30.41,10.20.30.42:1234/db");
	}

	protected AmqpServiceInfo createRabbitService(String id) {
		return new AmqpServiceInfo(id, "10.20.30.40", 1234, "username", "password", "vh");
	}
	
	protected RedisServiceInfo createRedisService(String id) {
		return new RedisServiceInfo(id, "host", 1234, "password");
	}

	//We can't use keyspaces to test cassandra as it expects to connect to the cluster. Other test scenarios use mocks to test it
	protected CassandraClusterServiceInfo createCassandraService(String id){
		return new CassandraClusterServiceInfo(id, Collections.singletonList("10.20.30.40"),"username","password","",9042);
	}
}
