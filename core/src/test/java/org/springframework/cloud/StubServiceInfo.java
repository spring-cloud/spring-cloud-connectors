package org.springframework.cloud;

import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("stub")
public class StubServiceInfo extends UriBasedServiceInfo {
	public StubServiceInfo(String id, String host, int port, String username, String password) {
		super(id, "stub", host, port, username, password, null);
	}
	
	// To test the scenario, where the name attribute of a property is explicitly specified 
	@ServiceProperty(name="bar")
	public String getFoo() {
		return "foo";
	}
}