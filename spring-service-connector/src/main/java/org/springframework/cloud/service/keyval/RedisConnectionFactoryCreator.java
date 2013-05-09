package org.springframework.cloud.service.keyval;

import static org.springframework.cloud.service.Util.hasClass;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * Simplified access to creating Redis service objects.
 *
 * @author Ramnivas Laddad
 * @author Jennifer Hickey
 * @author Thomas Risberg
 *
 */
public class RedisConnectionFactoryCreator extends AbstractServiceConnectorCreator<RedisConnectionFactory, RedisServiceInfo> {

	private static final String REDIS_CLIENT_CLASS_NAME = "redis.clients.jedis.Jedis";

	RedisConnectionFactoryConfigurer configurer = new RedisConnectionFactoryConfigurer();

	@Override
	public RedisConnectionFactory create(RedisServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		if (hasClass(REDIS_CLIENT_CLASS_NAME)) {
			JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
			connectionFactory.setHostName(serviceInfo.getHost());
			connectionFactory.setPort(serviceInfo.getPort());
			connectionFactory.setPassword(serviceInfo.getPassword());
			configurer.configure(connectionFactory, (PooledServiceConnectorConfig) serviceConnectorConfig);
			connectionFactory.afterPropertiesSet();
			return connectionFactory;
		} else {
			throw new ServiceConnectorCreationException("Failed to created cloud Redis connection factory for "
					+ serviceInfo.getId() + " service.  Jedis client implementation class ("
					+ REDIS_CLIENT_CLASS_NAME + ") not found");
		}
	}
}
