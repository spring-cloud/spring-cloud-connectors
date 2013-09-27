package org.springframework.cloud.config.java;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.document.MongoDbFactoryConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * JavaConfig base class for simplified access to the bound services.
 * 
 * 
 * Note that this class doesn't directly expose low-level methods such as getServiceInfo()
 * to keep focus on typical needs of a Spring application. But, since it exposes the cloud object itself, 
 * you can always call methods on it to get that kind of information.
 * 
 * @author Ramnivas Laddad
 *
 */
@Configuration
public class AbstractCloudConfig implements BeanFactoryAware {

	private static final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";
	
	private BeanFactory beanFactory;
	private CloudFactory cloudFactory;

	/**
	 * Get the cloud factory.
	 * 
	 * Most applications will never need this method, but provided here to cover corner cases.
	 * 
	 * Implementation note: This roundabout way of implementation is required to ensure that
	 * a CloudFoundry bean if created in some other configuration is available, we should use
	 * that.
	 *  
	 * @return
	 */
	protected CloudFactory cloudFactory() {
		if (cloudFactory == null) {
			cloudFactory = beanFactory.getBean(CloudFactory.class);
			if (cloudFactory == null) {
				cloudFactory = new CloudFactory();
				((SingletonBeanRegistry)beanFactory).registerSingleton(CLOUD_FACTORY_BEAN_NAME, cloudFactory);
			}
		}
		return cloudFactory;
	}

	@Bean
	public Cloud cloud() {
		return cloudFactory().getCloud();
	}
	
	// Relational database
	public DataSource dataSource() {
		return dataSource((DataSourceConfig)null);
	}
	
	public DataSource dataSource(DataSourceConfig dataSourceConfig) {
		return cloud().getSingletonServiceConnector(DataSource.class, dataSourceConfig);
	}

	public DataSource dataSource(String serviceId) {
		return dataSource(serviceId, null);
	}

	public DataSource dataSource(String serviceId, DataSourceConfig dataSourceConfig) {
		return cloud().getServiceConnector(serviceId, DataSource.class, dataSourceConfig);
	}

	// Mongodb
	public MongoDbFactory mongoDbFactory() {
		return mongoDbFactory((MongoDbFactoryConfig)null);
	}
	
	public MongoDbFactory mongoDbFactory(MongoDbFactoryConfig mongoDbFactoryConfig) {
		return cloud().getSingletonServiceConnector(MongoDbFactory.class, mongoDbFactoryConfig);
	}

	public MongoDbFactory mongoDbFactory(String serviceId) {
		return mongoDbFactory(serviceId, null);
	}

	public MongoDbFactory mongoDbFactory(String serviceId, MongoDbFactoryConfig mongoDbFactoryConfig) {
		return cloud().getServiceConnector(serviceId, MongoDbFactory.class, mongoDbFactoryConfig);
	}

	// RabbitMQ
	public ConnectionFactory rabbitConnectionFactory() {
		return rabbitConnectionFactory((RabbitConnectionFactoryConfig)null);
	}
	
	public ConnectionFactory rabbitConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
		return cloud().getSingletonServiceConnector(ConnectionFactory.class, rabbitConnectionFactoryConfig);
	}

	public ConnectionFactory rabbitConnectionFactory(String serviceId) {
		return rabbitConnectionFactory(serviceId, null);
	}

	public ConnectionFactory rabbitConnectionFactory(String serviceId, RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
		return cloud().getServiceConnector(serviceId, ConnectionFactory.class, rabbitConnectionFactoryConfig);
	}
	
	// Redis
	public RedisConnectionFactory redisConnectionFactory() {
		return redisConnectionFactory((PooledServiceConnectorConfig)null);
	}
	
	public RedisConnectionFactory redisConnectionFactory(PooledServiceConnectorConfig redisConnectionFactoryConfig) {
		return cloud().getSingletonServiceConnector(RedisConnectionFactory.class, redisConnectionFactoryConfig);
	}

	public RedisConnectionFactory redisConnectionFactory(String serviceId) {
		return redisConnectionFactory(serviceId, null);
	}

	public RedisConnectionFactory redisConnectionFactory(String serviceId, PooledServiceConnectorConfig redisConnectionFactoryConfig) {
		return cloud().getServiceConnector(serviceId, RedisConnectionFactory.class, redisConnectionFactoryConfig);
	}
	
	// Generic service
	public Object service() {
		return service(Object.class);
	}

	public <T> T service(Class<T> serviceConnectorType) {
		return cloud().getSingletonServiceConnector(serviceConnectorType, null);
	}

	public Object service(String serviceId) {
		return service(serviceId, Object.class);
	}

	public <T> T service(String serviceId, Class<T> serviceConnectorType) {
		return cloud().getServiceConnector(serviceId, serviceConnectorType, null);
	}
	
	public Properties properties() {
		return cloud().getCloudProperties();
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
