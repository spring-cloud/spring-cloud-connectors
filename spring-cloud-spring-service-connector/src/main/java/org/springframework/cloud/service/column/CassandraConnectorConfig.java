package org.springframework.cloud.service.column;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * @author Vinicius Carvalho
 */
public class CassandraConnectorConfig implements ServiceConnectorConfig{

    private RetryPolicy retryPolicy;
    private ReconnectionPolicy reconnectionPolicy;
    private LoadBalancingPolicy loadBalancingPolicy;
    private ProtocolOptions.Compression compression;
    private SocketOptions socketOptions;
    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public CassandraConnectorConfig(){}

    public CassandraConnectorConfig(RetryPolicy retryPolicy, ReconnectionPolicy reconnectionPolicy, LoadBalancingPolicy loadBalancingPolicy, ProtocolOptions.Compression compression, SocketOptions socketOptions) {
        this.retryPolicy = retryPolicy;
        this.reconnectionPolicy = reconnectionPolicy;
        this.loadBalancingPolicy = loadBalancingPolicy;
        this.compression = compression;
        this.socketOptions = socketOptions;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public ReconnectionPolicy getReconnectionPolicy() {
        return reconnectionPolicy;
    }

    public void setReconnectionPolicy(ReconnectionPolicy reconnectionPolicy) {
        this.reconnectionPolicy = reconnectionPolicy;
    }

    public LoadBalancingPolicy getLoadBalancingPolicy() {
        return loadBalancingPolicy;
    }

    public void setLoadBalancingPolicy(LoadBalancingPolicy loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    public ProtocolOptions.Compression getCompression() {
        return compression;
    }

    public void setCompression(ProtocolOptions.Compression compression) {
        this.compression = compression;
    }

    public SocketOptions getSocketOptions() {
        return socketOptions;
    }

    public void setSocketOptions(SocketOptions socketOptions) {
        this.socketOptions = socketOptions;
    }





}
