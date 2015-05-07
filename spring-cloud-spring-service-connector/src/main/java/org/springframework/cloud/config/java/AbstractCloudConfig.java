package org.springframework.cloud.config.java;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
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
 * Note that this class doesn't directly expose low-level methods such as getServiceInfo()
 * to keep focus on typical needs of a Spring application. But, since it exposes the cloud object itself,
 * you can always call methods on it to get that kind of information.
 *
 * @author Ramnivas Laddad
 *
 */
@Configuration
public abstract class AbstractCloudConfig implements BeanFactoryAware {

	private static final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";

	private CloudFactory cloudFactory;

	private Cloud cloud;

	private ServiceConnectionFactory connectionFactory;

	/**
	 * Get the cloud factory.
	 *
	 * Most applications will never need this method, but provided here to cover corner cases.
	 *
	 * @return cloud factory
	 */
	protected CloudFactory cloudFactory() {
		return cloudFactory;
	}

	/**
	 * Get the underlying cloud object.
	 *
	 * @return the cloud object appropriate for the current environment
	 */
	@Bean
	public Cloud cloud() {
		return cloud;
	}

	public ServiceConnectionFactory connectionFactory() {
		return connectionFactory;
	}

	/**
	 * Get the object containing service and app properties
	 *
	 * @return the properties of the discovered runtime environment
	 */
	public Properties properties() {
		return cloud().getCloudProperties();
	}

	/**
	 * Implementation note: This roundabout way of implementation is required to ensure that
	 * a {@link CloudFactory} bean if created in some other configuration is available, we should use
	 * that.
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (cloudFactory == null) {
			try {
				cloudFactory = beanFactory.getBean(CloudFactory.class);
			} catch (NoSuchBeanDefinitionException ex) {
				cloudFactory = new CloudFactory();
				((SingletonBeanRegistry) beanFactory).registerSingleton(CLOUD_FACTORY_BEAN_NAME, cloudFactory);
			}
		}
		this.cloud = cloudFactory.getCloud();
		this.connectionFactory = new ServiceConnectionFactory();
	}

	public class ServiceConnectionFactory {
		// Relational database

		/**
		 * Get the {@link DataSource} object associated with the only relational database service bound to the app.
		 *
		 * This is equivalent to the {@code <cloud:data-source/>} element.
		 *
		 * @return data source
		 * @throws CloudException
		 *             if there are either 0 or more than 1 relational database services.
		 */
		public DataSource dataSource() {
			return dataSource((DataSourceConfig) null);
		}

		/**
		 * Get the {@link DataSource} object associated with the only relational database service bound to the app
		 * configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:data-source>} element with nested {@code <cloud:connection>} and/or
		 * {@code <cloud:pool>} elements.
		 *
		 * @param dataSourceConfig
		 *            configuration for the data source created
		 * @return data source
		 * @throws CloudException
		 *             if there are either 0 or more than 1 relational database services.
		 */
		public DataSource dataSource(DataSourceConfig dataSourceConfig) {
			return cloud.getSingletonServiceConnector(DataSource.class, dataSourceConfig);
		}

		/**
		 * Get the {@link DataSource} object for the specified relational database service.
		 *
		 * This is equivalent to the {@code <cloud:data-source service-id="serviceId"/>}
		 *
		 * @param serviceId
		 *            the name of the service
		 * @return data source
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public DataSource dataSource(String serviceId) {
			return dataSource(serviceId, null);
		}

		/**
		 * Get the {@link DataSource} object for the specified relational database service configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:data-source service-id="serviceId"/>} element with
		 * nested {@code <cloud:connection>} and/or {@code <cloud:pool>} elements.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @param dataSourceConfig
		 *            configuration for the data source created
		 * @return data source
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public DataSource dataSource(String serviceId, DataSourceConfig dataSourceConfig) {
			return cloud.getServiceConnector(serviceId, DataSource.class, dataSourceConfig);
		}

		// Mongodb
		/**
		 * Get the {@link MongoDbFactory} object associated with the only MongoDB service bound to the app.
		 *
		 * This is equivalent to the {@code <cloud:mongo-db-factory/>} element.
		 *
		 * @return mongo db factory
		 * @throws CloudException
		 *             if there are either 0 or more than 1 mongodb services.
		 */
		public MongoDbFactory mongoDbFactory() {
			return mongoDbFactory((MongoDbFactoryConfig) null);
		}

		/**
		 * Get the {@link MongoDbFactory} object associated with the only MongoDB service bound to the app
		 * configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:mongo-db-factory>} element with a nested {@code <cloud:mongo-options>} element.
		 *
		 * @param mongoDbFactoryConfig
		 *            configuration for the mondo db factory created
		 * @return mongo db factory
		 * @throws CloudException
		 *             if there are either 0 or more than 1 mongodb services.
		 */
		public MongoDbFactory mongoDbFactory(MongoDbFactoryConfig mongoDbFactoryConfig) {
			return cloud.getSingletonServiceConnector(MongoDbFactory.class, mongoDbFactoryConfig);
		}

		/**
		 * Get the {@link MongoDbFactory} object for the specified MongoDB service.
		 *
		 * This is equivalent to the {@code <cloud:mongo-db-factory service-id="serviceId">} element.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @return mongo db factory
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public MongoDbFactory mongoDbFactory(String serviceId) {
			return mongoDbFactory(serviceId, null);
		}

		/**
		 * Get the {@link MongoDbFactory} object for the specified MongoDB service configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:mongo-db-factory service-id="serviceId">} element
		 * with a nested {@code <cloud:mongo-options>} element.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @param mongoDbFactoryConfig
		 *            configuration for the mongo db factory created
		 * @return mongo db factory
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public MongoDbFactory mongoDbFactory(String serviceId, MongoDbFactoryConfig mongoDbFactoryConfig) {
			return cloud.getServiceConnector(serviceId, MongoDbFactory.class, mongoDbFactoryConfig);
		}

		// RabbitMQ
		/**
		 * Get the {@link ConnectionFactory} object associated with the only RabbitMQ service bound to the app.
		 *
		 * This is equivalent to the {@code <cloud:rabbit-connection-factory>} element.
		 *
		 * @return rabbit connection factory
		 * @throws CloudException
		 *             if there are either 0 or more than 1 RabbitMQ services.
		 */
		public ConnectionFactory rabbitConnectionFactory() {
			return rabbitConnectionFactory((RabbitConnectionFactoryConfig) null);
		}

		/**
		 * Get the {@link ConnectionFactory} object associated with the only RabbitMQ service bound to the app
		 * configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:rabbit-connection-factory>} element
		 * with a nested {@code <cloud:rabbit-options>} element.
		 *
		 * @param rabbitConnectionFactoryConfig
		 *            configuration for the rabbit connection factory created
		 * @return rabbit connection factory
		 * @throws CloudException
		 *             if there are either 0 or more than 1 RabbitMQ services.
		 */
		public ConnectionFactory rabbitConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
			return cloud.getSingletonServiceConnector(ConnectionFactory.class, rabbitConnectionFactoryConfig);
		}

		/**
		 * Get the {@link ConnectionFactory} object for the specified RabbitMQ service.
		 *
		 * This is equivalent to the {@code <cloud:rabbit-connection-factory service-id="serviceId">} element.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @return rabbit connection factory
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public ConnectionFactory rabbitConnectionFactory(String serviceId) {
			return rabbitConnectionFactory(serviceId, null);
		}

		/**
		 * Get the {@link ConnectionFactory} object for the specified RabbitMQ service configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:rabbit-connection-factory service-id="serviceId">} element
		 * with a nested {@code <cloud:rabbit-options>} element.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @param rabbitConnectionFactoryConfig
		 *            configuration for the {@link ConnectionFactory} created
		 * @return rabbit connection factory
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public ConnectionFactory rabbitConnectionFactory(String serviceId,
			RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
			return cloud.getServiceConnector(serviceId, ConnectionFactory.class, rabbitConnectionFactoryConfig);
		}

		// Redis
		/**
		 * Get the {@link RedisConnectionFactory} object associated with the only Redis service bound to the app.
		 *
		 * This is equivalent to the {@code <cloud:redis-connection-factory/>} element
		 *
		 * @return redis connection factory
		 * @throws CloudException
		 *             if there are either 0 or more than 1 redis services.
		 */
		public RedisConnectionFactory redisConnectionFactory() {
			return redisConnectionFactory((PooledServiceConnectorConfig) null);
		}

		/**
		 * Get the {@link RedisConnectionFactory} object associated with the only Redis service bound to the app
		 * configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:redis-connection-factory service-id="serviceId">} element
		 * with a nested {@code <cloud:pool>} element.
		 *
		 * @param redisConnectionFactoryConfig
		 *            configuration for the {@link RedisConnectionFactory} created
		 * @return redis connection factory
		 * @throws CloudException
		 *             if there are either 0 or more than 1 redis services.
		 */
		public RedisConnectionFactory redisConnectionFactory(PooledServiceConnectorConfig redisConnectionFactoryConfig) {
			return cloud.getSingletonServiceConnector(RedisConnectionFactory.class, redisConnectionFactoryConfig);
		}

		/**
		 * Get the {@link RedisConnectionFactory} object for the specified Redis service.
		 *
		 * This is equivalent to the {@code <cloud:redis-connection-factory service-id="serviceId">} element.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @return redis connection factory
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public RedisConnectionFactory redisConnectionFactory(String serviceId) {
			return redisConnectionFactory(serviceId, null);
		}

		/**
		 * Get the {@link RedisConnectionFactory} object for the specified Redis service configured as specified.
		 *
		 * This is equivalent to the {@code <cloud:redis-connection-factory service-id="serviceId">} element
		 * with a nested {@code <cloud:pool>} element.
		 *
		 * @param serviceId
		 *            the name of the service
		 * @param redisConnectionFactoryConfig
		 *            configuration for the {@link RedisConnectionFactory} created
		 * @return redis connection factory
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public RedisConnectionFactory redisConnectionFactory(String serviceId,
			PooledServiceConnectorConfig redisConnectionFactoryConfig) {
			return cloud.getServiceConnector(serviceId, RedisConnectionFactory.class, redisConnectionFactoryConfig);
		}

		// Generic service
		/**
		 * Get the service connector object associated with the only service bound to the app.
		 *
		 * This is equivalent to the {@code <cloud:service/>} element.
		 *
		 * @return service connector object
		 * @throws CloudException
		 *             if there are either 0 or more than 1 services.
		 */
		public Object service() {
			return service(Object.class);
		}

		/**
		 * Get the service connector object of the specified type if there is only one such candidate service
		 *
		 * This is equivalent to the {@code <cloud:service connector-type="T.class"/>} element.
		 *
		 * @param <T>
		 *            the type of the service connector to be returned
		 * @param serviceConnectorType
		 *            the class of the service connector to be returned
		 * @return service connector object
		 * @throws CloudException
		 *             if there are either 0 or more than 1 candidate services.
		 */
		public <T> T service(Class<T> serviceConnectorType) {
			return cloud.getSingletonServiceConnector(serviceConnectorType, null);
		}

		/**
		 * Get the service connector object for the specified service.
		 *
		 * This is equivalent to the {@code <cloud:service service-id="serviceId"/>} element.
		 *
		 * @param serviceId
		 *            the service ID of the service to be returned
		 * @return service connector object
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public Object service(String serviceId) {
			return service(serviceId, Object.class);
		}

		/**
		 * Get the service connector object of the specified type and service ID
		 *
		 * This is equivalent to the {@code <cloud:service service-id="serviceId" connector-type="T.class"/>} element.
		 *
		 * @param serviceId
		 *            the service ID of the service to be returned
		 * @param <T>
		 *            the type of the service connector to be returned
		 * @param serviceConnectorType
		 *            the class of the service connector to be returned
		 * @return service connector object
		 * @throws CloudException
		 *             if the specified service doesn't exist
		 */
		public <T> T service(String serviceId, Class<T> serviceConnectorType) {
			return cloud.getServiceConnector(serviceId, serviceConnectorType, null);
		}

		public <T> T service(String serviceId, Class<T> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfig){
			return cloud.getServiceConnector(serviceId, serviceConnectorType, serviceConnectorConfig);
		}
	}
}
