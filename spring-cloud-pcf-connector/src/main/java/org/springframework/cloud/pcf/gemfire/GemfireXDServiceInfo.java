package org.springframework.cloud.pcf.gemfire;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.common.RelationalServiceInfo;


/**
 *  
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("gemfirexd")
public class GemfireXDServiceInfo extends RelationalServiceInfo {

    public static final String JDBC_URL_TYPE = "gemfirexd";

	public GemfireXDServiceInfo(String id, String url) {
		super(id, url, JDBC_URL_TYPE);
	}
}
