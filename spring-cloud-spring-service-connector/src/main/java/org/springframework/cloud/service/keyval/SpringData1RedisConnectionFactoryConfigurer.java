package org.springframework.cloud.service.keyval;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.MapServiceConnectionConfigurer;
import org.springframework.cloud.service.MapServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfigurer;
import org.springframework.cloud.service.Util;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class SpringData1RedisConnectionFactoryConfigurer implements ServiceConnectorConfigurer<JedisConnectionFactory, RedisConnectionFactoryConfig> {
    private MapServiceConnectionConfigurer<JedisConnectionFactory, MapServiceConnectorConfig> mapServiceConnectionConfigurer =
        new MapServiceConnectionConfigurer<JedisConnectionFactory, MapServiceConnectorConfig>();

    @Override
    public JedisConnectionFactory configure(JedisConnectionFactory connectionFactory, RedisConnectionFactoryConfig config) {
        if (config != null) {
            configurePool(connectionFactory, config);
            configureConnection(connectionFactory, config);
        }
        return connectionFactory;
    }

    public JedisConnectionFactory configure(JedisConnectionFactory connectionFactory, PooledServiceConnectorConfig config) {
        if (config != null) {
            configurePool(connectionFactory, config);
        }
        return connectionFactory;
    }

    private void configurePool(JedisConnectionFactory connectionFactory, PooledServiceConnectorConfig config) {
        if (config.getPoolConfig() != null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            BeanWrapper target = new BeanWrapperImpl(poolConfig);
            BeanWrapper source = new BeanWrapperImpl(config.getPoolConfig());
            Util.setCorrespondingProperties(target, source);
            connectionFactory.setPoolConfig(poolConfig);
        }
    }

    private void configureConnection(JedisConnectionFactory connectionFactory, RedisConnectionFactoryConfig config) {
        if (config.getConnectionProperties() != null) {
            mapServiceConnectionConfigurer.configure(connectionFactory, config.getConnectionProperties());
        }
    }

}
