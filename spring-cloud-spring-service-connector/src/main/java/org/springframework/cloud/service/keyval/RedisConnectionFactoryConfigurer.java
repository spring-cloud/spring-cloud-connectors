package org.springframework.cloud.service.keyval;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfigurer;
import org.springframework.cloud.service.Util;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RedisConnectionFactoryConfigurer implements ServiceConnectorConfigurer<JedisConnectionFactory, PooledServiceConnectorConfig> {

	@Override
	public JedisConnectionFactory configure(JedisConnectionFactory connectionFactory, PooledServiceConnectorConfig config) {
		if (config != null && config.getPoolConfig() != null) {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			BeanWrapper target = new BeanWrapperImpl(poolConfig);
			BeanWrapper source = new BeanWrapperImpl(config.getPoolConfig());
			Util.setCorrespondingProperties(target, source);
			connectionFactory.setPoolConfig(poolConfig);
		}
		return connectionFactory;
	}

}
