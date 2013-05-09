package org.springframework.cloud;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("stub")
public class StubServiceInfo extends BaseServiceInfo {
	public StubServiceInfo(String id, String host, int port, String username, String password) {
		super(id, host, port, username, password);
	}
	
	// To test the scenario, where the name attribute of a property is explicitly specified 
	@ServiceProperty(name="bar")
	public String getFoo() {
		return "foo";
	}
}