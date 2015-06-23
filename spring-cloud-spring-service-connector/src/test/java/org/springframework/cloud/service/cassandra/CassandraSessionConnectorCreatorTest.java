package org.springframework.cloud.service.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.cassandra.io.util.FileUtils;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.service.column.CassandraSessionCreator;
import org.springframework.cloud.service.common.CassandraClusterServiceInfo;

import java.io.File;
import java.util.Collections;

/**
 * Created by vcarvalho on 6/10/15.
 */
public class CassandraSessionConnectorCreatorTest {

    @Test
    public void withoutKeyspace() throws Exception{


        CassandraClusterServiceInfo info = new CassandraClusterServiceInfo("local", Collections.singletonList("127.0.0.1"),null,null,null,9142);
        CassandraSessionCreator creator = new CassandraSessionCreator();
        Session session = creator.create(info, null);

        Assert.assertNull(session.getLoggedKeyspace());

    }

    @Test
    public void withKeyspace() throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();

        CassandraClusterServiceInfo info = new CassandraClusterServiceInfo("local", Collections.singletonList("localhost"),null,null,"mykeyspace",9142);
        CQLDataSet dataSet = new ClassPathCQLDataSet("org/springframework.cloud/service.cassandra/cassandra-dataset.cql",info.getKeyspace());
        Session globalSession = Cluster.builder().addContactPoints(info.getNodes().get(0)).withPort(info.getCqlPort()).build().connect();
        CQLDataLoader dataLoader = new CQLDataLoader(globalSession);
        dataLoader.load(dataSet);


        CassandraSessionCreator creator = new CassandraSessionCreator();
        Session session = creator.create(info, null);

        Assert.assertEquals("mykeyspace", session.getLoggedKeyspace());

        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();

    }


    @AfterClass
    public static void cleanEmbeddedDirs() throws Exception{

        File dirFile = new File("target");
        if (dirFile.exists()) {
            FileUtils.deleteRecursive(new File("target"));
        }
    }


}
