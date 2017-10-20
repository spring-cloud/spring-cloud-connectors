package org.springframework.cloud.config.java;

import com.datastax.driver.core.Cluster;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.column.CassandraClusterConfig;
import org.springframework.cloud.service.document.MongoDbFactoryConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;

/**
 * @author Scott Frederick
 */
public interface ServiceConnectionFactory {
	DataSource dataSource();

	DataSource dataSource(DataSourceConfig dataSourceConfig);

	DataSource dataSource(String serviceId);

	DataSource dataSource(String serviceId, DataSourceConfig dataSourceConfig);

	MongoDbFactory mongoDbFactory();

	MongoDbFactory mongoDbFactory(MongoDbFactoryConfig mongoDbFactoryConfig);

	MongoDbFactory mongoDbFactory(String serviceId);

	MongoDbFactory mongoDbFactory(String serviceId, MongoDbFactoryConfig mongoDbFactoryConfig);

	ConnectionFactory rabbitConnectionFactory();

	ConnectionFactory rabbitConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);

	ConnectionFactory rabbitConnectionFactory(String serviceId);

	ConnectionFactory rabbitConnectionFactory(String serviceId,
											  RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);

	RedisConnectionFactory redisConnectionFactory();

	RedisConnectionFactory redisConnectionFactory(PooledServiceConnectorConfig redisConnectionFactoryConfig);

	RedisConnectionFactory redisConnectionFactory(String serviceId);

	RedisConnectionFactory redisConnectionFactory(String serviceId,
												  PooledServiceConnectorConfig redisConnectionFactoryConfig);

	/**
	 * Get the {@link Cluster} object associated with the only Cassandra service bound to
	 * the app.
	 *
	 * @return the Cassandra {@link Cluster}
	 * @throws org.springframework.cloud.CloudException if there are either 0 or more than
	 * 1 Cassandra database services.
	 */
	Cluster cluster();

	/**
	 * Get the {@link Cluster} object associated with the only Cassandra service bound to
	 * the app configured as specified.
	 *
	 * @param config configuration for the Cluster created
	 * @return the Cassandra {@link Cluster}
	 * @throws org.springframework.cloud.CloudException if there are either 0 or more than
	 * 1 Cassandra database services.
	 */
	Cluster cluster(CassandraClusterConfig config);

	/**
	 * Get the {@link Cluster} object associated with the Cassandra {@code serviceId}
	 * bound to the app.
	 *
	 * @param serviceId the Cassandra serviceId
	 * @return the Cassandra {@link Cluster}
	 * @throws org.springframework.cloud.CloudException if there is no service bound with
	 * the {@code serviceId}
	 */
	Cluster cluster(String serviceId);

	/**
	 * Get the {@link Cluster} object associated with the Cassandra {@code serviceId}
	 * bound to the app configured as specified.
	 *
	 * @param serviceId the Cassandra serviceId
	 * @param config configuration for the Cluster created
	 * @return the Cassandra {@link Cluster}
	 * @throws org.springframework.cloud.CloudException if there is no service bound with
	 * the {@code serviceId}
	 */
	Cluster cluster(String serviceId, CassandraClusterConfig config);

	Object service();

	<T> T service(Class<T> serviceConnectorType);

	Object service(String serviceId);

	<T> T service(String serviceId, Class<T> serviceConnectorType);

	<T> T service(String serviceId, Class<T> serviceConnectorType, ServiceConnectorConfig serviceConnectorConfig);
}
