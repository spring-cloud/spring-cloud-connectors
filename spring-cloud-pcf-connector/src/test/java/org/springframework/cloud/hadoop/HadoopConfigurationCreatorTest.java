package org.springframework.cloud.hadoop;

import static org.junit.Assert.*;

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.springframework.cloud.pcf.hadoop.HadoopConfigurationCreator;
import org.springframework.cloud.pcf.hadoop.HadoopServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class HadoopConfigurationCreatorTest {
    private HadoopConfigurationCreator creator = new HadoopConfigurationCreator(); 
    
    @Test
    public void hadoopConfigurationCreation() {
        HadoopServiceInfo serviceInfo = new HadoopServiceInfo("phd-hadoop", "hdfs://hdfshost:1234", "yrm:2345", "yrsm:3456");
        Configuration configuration = creator.create(serviceInfo, null);
        assertEquals("hdfs://hdfshost:1234", configuration.get("fs.defaultFS"));
        assertEquals("yrm:2345", configuration.get("yarn.resourcemanager.address"));
        assertEquals("yrsm:3456", configuration.get("yarn.resourcemanager.scheduler.address"));
    }
}
