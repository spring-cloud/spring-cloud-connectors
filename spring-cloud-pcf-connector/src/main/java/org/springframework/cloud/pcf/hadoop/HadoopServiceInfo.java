package org.springframework.cloud.pcf.hadoop;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;

/**
 * Hadoop service info (also considers yarn properties).
 * 
 * @author Ramnivas Laddad
 *
 */
public class HadoopServiceInfo extends BaseServiceInfo {

    private String defaultHdfsUri;
    private String yarnResourceManagerAddress;
    private String yarnResourceManagerSchedulerAddress;

    public HadoopServiceInfo(String id, String defaultHdfsUri, 
                             String yarnResourceManagerAddress, String yarnResourceManagerSchedulerAddress) {
        super(id);
        this.defaultHdfsUri = defaultHdfsUri;
        this.yarnResourceManagerAddress = yarnResourceManagerAddress;
        this.yarnResourceManagerSchedulerAddress = yarnResourceManagerSchedulerAddress;
    }

    
    @ServiceInfo.ServiceProperty(category = "connection")
    public String getDefaultFS() {
        return defaultHdfsUri;
    }

    @ServiceInfo.ServiceProperty(category = "connection")
    public String getYarnResourceManagerAddress() {
        return yarnResourceManagerAddress;
    }

    @ServiceInfo.ServiceProperty(category = "connection")
    public String getYarnResourceManagerSchedulerAddress() {
        return yarnResourceManagerSchedulerAddress;
    }

}
