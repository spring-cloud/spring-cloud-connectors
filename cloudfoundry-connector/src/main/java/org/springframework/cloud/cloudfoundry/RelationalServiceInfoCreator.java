package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.util.UriInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {

	public RelationalServiceInfoCreator(String tag, String uriScheme) {
		super(tag, uriScheme);
	}

	public abstract SI createServiceInfo(String id, String uri);
	
	protected String getConnectionScheme() {
		 // by default return the tag as the uri scheme
		return getTag();
	}
	
	public SI createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");
		
		String uri = null;
		if (credentials.containsKey("uri")) {
			uri = credentials.get("uri").toString();
		} else {
			String host = (String) credentials.get("host");
			int port = Integer.parseInt(credentials.get("port").toString()); // allows the port attribute to be quoted or plain

			String username = (String) credentials.get("user");
			String password = (String) credentials.get("password");

			String database = (String) credentials.get("name");
			
			uri = new UriInfo(getConnectionScheme(), host, port, username, password, database).toString();
		}
		return createServiceInfo(id, uri);
	}
}
