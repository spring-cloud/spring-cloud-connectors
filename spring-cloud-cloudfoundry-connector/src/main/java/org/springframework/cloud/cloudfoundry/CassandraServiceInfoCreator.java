package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.CassandraClusterServiceInfo;

import java.util.List;
import java.util.Map;

/**
 * @author  Vinicius Carvalho
 */
public class CassandraServiceInfoCreator extends CloudFoundryServiceInfoCreator<CassandraClusterServiceInfo>{

    public CassandraServiceInfoCreator(){
        super(new Tags("cassandra"));
    }

    public CassandraServiceInfoCreator(Tags tags, String... uriSchemes) {
        super(tags);
    }

    @Override
    public CassandraClusterServiceInfo createServiceInfo(Map<String, Object> stringObjectMap) {
        Map<String,Object> credentials = (Map<String, Object>) stringObjectMap.get("credentials");
        String id = (String) stringObjectMap.get("name");
        String keyspace = getStringFromCredentials(credentials,"keyspace_name");
        String username = getStringFromCredentials(credentials,"username");
        String password = getStringFromCredentials(credentials,"password");
        Integer thriftPort = (Integer) credentials.get("thrift_port");
        Integer cqlPort = (Integer) credentials.get("cql_port");
        List<String> nodeIps = (List<String>) credentials.get("node_ips");

        return new CassandraClusterServiceInfo(id,nodeIps,username,password,keyspace,cqlPort);
    }
}
