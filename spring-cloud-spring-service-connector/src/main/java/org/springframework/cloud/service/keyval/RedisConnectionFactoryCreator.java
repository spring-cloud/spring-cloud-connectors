package org.springframework.cloud.service.keyval;

import static org.springframework.cloud.service.Util.hasClass;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Simplified access to creating Redis service objects.
 * Supports Jedis and lettuce Redis clients.
 *
 * @author Ramnivas Laddad
 * @author Jennifer Hickey
 * @author Thomas Risberg
 * @author Mark Paluch
 */
public class RedisConnectionFactoryCreator extends AbstractServiceConnectorCreator<RedisConnectionFactory, RedisServiceInfo> {

	private static final String JEDIS_CLASS_NAME = "redis.clients.jedis.Jedis";
	private static final String LETTUCE_CLASS_NAME = "com.lambdaworks.redis.RedisClient";

	@Override
	public RedisConnectionFactory create(RedisServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {

		if (hasClass(JEDIS_CLASS_NAME)) {

			RedisConnectionFactoryConfigurer configurer = new RedisConnectionFactoryConfigurer();

			JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
			connectionFactory.setHostName(serviceInfo.getHost());
			connectionFactory.setPort(serviceInfo.getPort());
			connectionFactory.setPassword(serviceInfo.getPassword());

			if (serviceConnectorConfig instanceof RedisConnectionFactoryConfig) {
				configurer.configure(connectionFactory, (RedisConnectionFactoryConfig) serviceConnectorConfig);
			} else {
				configurer.configure(connectionFactory, (PooledServiceConnectorConfig) serviceConnectorConfig);
			}

			connectionFactory.afterPropertiesSet();
			return connectionFactory;
		}
		else if (hasClass(LETTUCE_CLASS_NAME)) {

			RedisLettuceConnectionFactoryConfigurer configurer = new RedisLettuceConnectionFactoryConfigurer();

			LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
			connectionFactory.setHostName(serviceInfo.getHost());
			connectionFactory.setPort(serviceInfo.getPort());
			connectionFactory.setPassword(serviceInfo.getPassword());

			configurer.configure(connectionFactory, (RedisConnectionFactoryConfig) serviceConnectorConfig);

			connectionFactory.afterPropertiesSet();
			return connectionFactory;
		}
		else {
			throw new ServiceConnectorCreationException(String.format("Failed to create cloud Redis connection factory " +
					"for %s service. No client implementation classes "
					+ " of jedis or lettuce clients implementation (%s, %s) not found", serviceInfo.getId(),
					JEDIS_CLASS_NAME, LETTUCE_CLASS_NAME));
		}
	}
}
