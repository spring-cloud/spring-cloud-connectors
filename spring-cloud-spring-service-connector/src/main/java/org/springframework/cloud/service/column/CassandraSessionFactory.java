package org.springframework.cloud.service.column;

import com.datastax.driver.core.Session;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * @author Vinicius Carvalho
 */
public class CassandraSessionFactory extends AbstractCloudServiceConnectorFactory<Session> {
    public CassandraSessionFactory(String serviceId,ServiceConnectorConfig serviceConnectorConfiguration) {
        super(serviceId, Session.class, serviceConnectorConfiguration);
    }
}
