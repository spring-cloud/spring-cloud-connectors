package org.springframework.cloud.service.keyval;

import static org.springframework.cloud.service.Util.hasClass;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder;

/**
 * Simplified access to creating Redis service objects.
 * Supports Jedis and lettuce Redis clients.
 *
 * @author Ramnivas Laddad
 * @author Jennifer Hickey
 * @author Thomas Risberg
 * @author Mark Paluch
 * @author Scott Frederick
 */
public class RedisConnectionFactoryCreator extends AbstractServiceConnectorCreator<RedisConnectionFactory, RedisServiceInfo> {

	private static final String JEDIS_CLASS_NAME = "redis.clients.jedis.Jedis";
	private static final String LETTUCE_CLASS_NAME = "io.lettuce.core.RedisClient";

	@Override
	public RedisConnectionFactory create(RedisServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(serviceInfo.getHost());
		configuration.setPort(serviceInfo.getPort());
		configuration.setPassword(RedisPassword.of(serviceInfo.getPassword()));

		if (hasClass(JEDIS_CLASS_NAME)) {
			JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();

			RedisJedisClientConfigurer clientConfigurer = new RedisJedisClientConfigurer();
			if (serviceConnectorConfig instanceof RedisConnectionFactoryConfig) {
				clientConfigurer.configure(builder, (RedisConnectionFactoryConfig) serviceConnectorConfig);
			} else {
				clientConfigurer.configure(builder, (PooledServiceConnectorConfig) serviceConnectorConfig);
			}

			JedisConnectionFactory connectionFactory = new JedisConnectionFactory(configuration, builder.build());
			connectionFactory.afterPropertiesSet();
			return connectionFactory;
		}
		else if (hasClass(LETTUCE_CLASS_NAME)) {
			LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();

			RedisLettuceClientConfigurer clientConfigurer = new RedisLettuceClientConfigurer();
			if (serviceConnectorConfig instanceof RedisConnectionFactoryConfig) {
				clientConfigurer.configure(builder, (RedisConnectionFactoryConfig) serviceConnectorConfig);
			} else {
				clientConfigurer.configure(builder, (PooledServiceConnectorConfig) serviceConnectorConfig);
			}

			LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, builder.build());
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
