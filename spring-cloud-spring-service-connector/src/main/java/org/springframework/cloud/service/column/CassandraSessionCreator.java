package org.springframework.cloud.service.column;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.CassandraClusterServiceInfo;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author Vinicius Carvalho
 */
public class CassandraSessionCreator extends AbstractServiceConnectorCreator<Session, CassandraClusterServiceInfo> {


    @Override
    public Session create(CassandraClusterServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
        Cluster cluster = createClusterConnection(serviceInfo,(CassandraConnectorConfig)serviceConnectorConfig);
        Session session = (StringUtils.isEmpty(serviceInfo.getKeyspace())) ? cluster.connect() : cluster.connect(serviceInfo.getKeyspace());

        return session;
    }

    private Cluster createClusterConnection(CassandraClusterServiceInfo serviceInfo, CassandraConnectorConfig config){
        Cluster.Builder builder = Cluster.builder();

        builder.addContactPoints(serviceInfo.getNodes().toArray(new String[]{})).withPort(serviceInfo.getCqlPort());

        if(!StringUtils.isEmpty(serviceInfo.getUserName())){
            builder.withCredentials(serviceInfo.getUserName(),serviceInfo.getPassword());
        }

        if(config != null && config.getCompression() != null){
            builder.withCompression(config.getCompression());
        }

        if(config != null && config.getLoadBalancingPolicy() != null){
            builder.withLoadBalancingPolicy(config.getLoadBalancingPolicy());
        }

        if(config != null && config.getPoolingOptions() != null){
            builder.withPoolingOptions(config.getPoolingOptions());
        }

        if(config != null && config.getReconnectionPolicy() != null){
            builder.withReconnectionPolicy(config.getReconnectionPolicy());
        }

        if(config != null && config.getRetryPolicy() != null){
            builder.withRetryPolicy(config.getRetryPolicy());
        }


        return builder.build();
    }


}
