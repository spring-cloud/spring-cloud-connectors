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

	public RelationalServiceInfoCreator(Tags tags, String... uriSchemes) {
		super(tags, uriSchemes);
	}

	public abstract SI createServiceInfo(String id, String uri);

	public SI createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String,Object> credentials = getCredentials(serviceData);
		String uri = getUriFromCredentials(credentials);

		if (uri == null) {
			String host = getStringFromCredentials(credentials, "hostname", "host");
			int port = getIntFromCredentials(credentials, "port");

			String username = getStringFromCredentials(credentials, "user", "username");
			String password = (String) credentials.get("password");

			String database = (String) credentials.get("name");
			
			uri = new UriInfo(getDefaultUriScheme(), host, port, username, password, database).toString();
		}

		return createServiceInfo(id, uri);
	}
}
