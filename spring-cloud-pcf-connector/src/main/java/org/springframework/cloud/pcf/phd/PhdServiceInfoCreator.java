package org.springframework.cloud.pcf.phd;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.pcf.gemfire.GemfireXDServiceInfo;
import org.springframework.cloud.pcf.hadoop.HadoopServiceInfo;
import org.springframework.cloud.service.BaseCompositeServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PhdServiceInfoCreator extends CloudFoundryServiceInfoCreator<BaseCompositeServiceInfo> {

	public PhdServiceInfoCreator() {
		super(new Tags("p-hd"));
	}

	@SuppressWarnings("unchecked")
    public BaseCompositeServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		String id = (String) serviceData.get("name");

        Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
        
        ServiceInfo hadoopServiceInfo = createHadoopServiceInfo(id, credentials);
        ServiceInfo hawqServiceInfo = createHawqServiceInfo(id, credentials);
        ServiceInfo gemfirexdServiceInfo = createGemfireXDServiceInfo(id, credentials);
        
		return new BaseCompositeServiceInfo(id, hadoopServiceInfo, hawqServiceInfo, gemfirexdServiceInfo);
	}

	@SuppressWarnings("unchecked")
	private HadoopServiceInfo createHadoopServiceInfo(String id, Map<String,Object> credentials) {
        Map<String, Object> hdfs = (Map<String, Object>) credentials.get("hdfs");
        Map<String, Object> hdfsConfig = (Map<String, Object>) hdfs.get("configuration");
        String defaultHdfsUri = (String) hdfsConfig.get("fs.defaultFS");

        Map<String, Object> yarn = (Map<String, Object>) credentials.get("yarn");
        Map<String, Object> yarnConfig = (Map<String, Object>) yarn.get("configuration");
        String yarnResourceManagerAddress = (String) yarnConfig.get("yarn.resourcemanager.address");
        String yarnResourceManagerSchedulerAddress = (String) yarnConfig.get("yarn.resourcemanager.scheduler.address");
        
        return new HadoopServiceInfo(id + "/hadoop", defaultHdfsUri, 
                                     yarnResourceManagerAddress, yarnResourceManagerSchedulerAddress);
	}
	
	private RelationalServiceInfo createHawqServiceInfo(String id, Map<String,Object> credentials) {
	    String key = "hawq";
	    
	    return new PostgresqlServiceInfo(id + "/" + key, extractDataSourceUri(id, key, credentials));
	}

    private RelationalServiceInfo createGemfireXDServiceInfo(String id, Map<String,Object> credentials) {
        String key = "gemfirexd";
        
        return new GemfireXDServiceInfo(id + "/" + key, extractDataSourceUri(id, key, credentials));
    }
	
	private String extractDataSourceUri(String id, String key, Map<String,Object> credentials) {
	    @SuppressWarnings("unchecked")
	    Map<String, Object> hawq = (Map<String, Object>) credentials.get(key);
        
        String uri = (String) hawq.get("uri");
        
        String jdbcUriPrefix = "jdbc:";
        
        if (uri.startsWith(jdbcUriPrefix)) {
            uri.substring(jdbcUriPrefix.length());
        }
        
        return uri;
	}
}
