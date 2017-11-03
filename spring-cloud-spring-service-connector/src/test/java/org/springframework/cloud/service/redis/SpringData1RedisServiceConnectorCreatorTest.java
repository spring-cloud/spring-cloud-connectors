package org.springframework.cloud.service.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.cloud.service.keyval.SpringData1RedisConnectionFactoryCreator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

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

        assertEquals(serviceInfo.getHost(), ReflectionTestUtils.getField(connector, "hostName"));
        assertEquals(serviceInfo.getPort(), ReflectionTestUtils.getField(connector, "port"));
        assertEquals(serviceInfo.getPassword(), ReflectionTestUtils.getField(connector, "password"));
    }
}
