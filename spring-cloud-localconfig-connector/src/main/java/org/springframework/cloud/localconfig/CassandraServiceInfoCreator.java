package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.CassandraClusterServiceInfo;

/**
 * @author  Vinicius Carvalho
 */
public class CassandraServiceInfoCreator extends LocalConfigServiceInfoCreator<CassandraClusterServiceInfo>{

    public CassandraServiceInfoCreator(){
        super(CassandraClusterServiceInfo.CASSANDRA_SCHEME);
    }

    @Override
    public CassandraClusterServiceInfo createServiceInfo(String id, String uri) {
        return new CassandraClusterServiceInfo(id,uri);
    }
}
