package org.springframework.cloud.service.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.cloud.service.keyval.SpringData1RedisConnectionFactoryCreator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class SpringData1RedisServiceConnectorCreatorTest {
    private static final String TEST_HOST = "10.20.30.40";
    private static final int TEST_PORT = 1234;
    private static final String TEST_PASSWORD = "mypass";


    @Mock private RedisServiceInfo mockRedisServiceInfo;

    private SpringData1RedisConnectionFactoryCreator testCreator = new SpringData1RedisConnectionFactoryCreator();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cloudRedisCreationNoConfig() throws Exception {
        RedisServiceInfo serviceInfo = createServiceInfo();

        RedisConnectionFactory dataSource = testCreator.create(serviceInfo, null);

        assertConnectorProperties(serviceInfo, dataSource);
    }

    public RedisServiceInfo createServiceInfo() {
        when(mockRedisServiceInfo.getHost()).thenReturn(TEST_HOST);
        when(mockRedisServiceInfo.getPort()).thenReturn(TEST_PORT);
        when(mockRedisServiceInfo.getPassword()).thenReturn(TEST_PASSWORD);

        return mockRedisServiceInfo;
    }

    private void assertConnectorProperties(RedisServiceInfo serviceInfo, RedisConnectionFactory connector) {
        assertNotNull(connector);

        if (connector instanceof JedisConnectionFactory) {
            JedisConnectionFactory connectionFactory = (JedisConnectionFactory) connector;
            assertEquals(serviceInfo.getHost(), connectionFactory.getHostName());
            assertEquals(serviceInfo.getPort(), connectionFactory.getPort());
            assertEquals(serviceInfo.getPassword(), connectionFactory.getPassword());
        } else if (connector instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) connector;
            assertEquals(serviceInfo.getHost(), connectionFactory.getHostName());
            assertEquals(serviceInfo.getPort(), connectionFactory.getPort());
            assertEquals(serviceInfo.getPassword(), connectionFactory.getPassword());
        } else {
            fail("Expected RedisConnectionFactory of type " +
                    JedisConnectionFactory.class.getName() + " or " + LettuceConnectionFactory.class.getName() +
                    " but was of type " + connector.getClass().getName());
        }
    }

}
