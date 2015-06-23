package org.springframework.cloud.config.xml;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * @author Vinicius Carvalho
 */
public class CassandraSessionXmlConfigTest extends AbstractServiceXmlConfigTest<Session> {

    @Test
    public void cassandraSessionWithConfiguration() throws Exception{
        ApplicationContext testContext = getTestApplicationContext("cloud-cassandra-with-config.xml", createService("my-service"));
        Session session = testContext.getBean("cassandra-full-config", getConnectorType());
        Assert.assertNotNull(session.getCluster().getConfiguration().getSocketOptions());
        Assert.assertEquals(15000,session.getCluster().getConfiguration().getSocketOptions().getConnectTimeoutMillis());
        Assert.assertTrue(DefaultRetryPolicy.class.isAssignableFrom(session.getCluster().getConfiguration().getPolicies().getRetryPolicy().getClass()));
        Assert.assertTrue(RoundRobinPolicy.class.isAssignableFrom(session.getCluster().getConfiguration().getPolicies().getLoadBalancingPolicy().getClass()));
        Assert.assertTrue(ConstantReconnectionPolicy.class.isAssignableFrom(session.getCluster().getConfiguration().getPolicies().getReconnectionPolicy().getClass()));
    }

    @Override
    protected String getWithServiceIdContextFileName() {
        return "cloud-cassandra-with-service-id.xml";
    }

    @Override
    protected String getWithoutServiceIdContextFileName() {
        return "cloud-cassandra-without-service-id.xml";
    }

    @Override
    protected ServiceInfo createService(String id) {
        return createCassandraService(id);
    }

    @Override
    protected Class<Session> getConnectorType() {
        return Session.class;
    }
}
