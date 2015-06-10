package org.springframework.cloud.service.common;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author  Vinicius Carvalho
 */
@ServiceInfo.ServiceLabel("cassandra")
public class CassandraClusterServiceInfo extends UriBasedServiceInfo {

    public static final String CASSANDRA_SCHEME = "cassandra";
    private final Integer cqlPort;
    private String username;
    private String password;
    private String keyspace;
    private List<String> nodes;

    /**
     * To keep compatibility with local config, we simply set the URI for the service using the first node in the cluster.
     * The connector creator actually uses the full list of nodes when creating the cluster connection
     * @param id
     * @param nodes
     * @param username
     * @param password
     * @param keyspace
     * @param cqlPort
     */
    public CassandraClusterServiceInfo(String id, List<String> nodes, String username, String password, String keyspace, Integer cqlPort) {
        super(id, CASSANDRA_SCHEME, nodes.get(0),cqlPort,username,password,keyspace);
        this.nodes = nodes;
        this.username = username;
        this.password = password;
        this.keyspace = keyspace;
        this.cqlPort = cqlPort;
    }

    public CassandraClusterServiceInfo(String id, String uriString){
        super(id,uriString);
        this.nodes = Collections.singletonList(getUriInfo().getHost());
        this.username = getUriInfo().getUserName();
        this.password = getUriInfo().getPassword();
        this.cqlPort = getUriInfo().getPort();
        this.keyspace = getUriInfo().getPath();
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public List<String> getNodes() {
        return nodes;
    }
    public Integer getCqlPort() {
        return cqlPort;
    }
}
