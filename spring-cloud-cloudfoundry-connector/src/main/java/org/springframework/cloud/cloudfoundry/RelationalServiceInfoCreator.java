package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.util.UriInfo;

import static org.springframework.cloud.service.common.RelationalServiceInfo.JDBC_PREFIX;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {

	public RelationalServiceInfoCreator(Tags tags, String... uriSchemes) {
		super(tags, uriSchemes);
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return jdbcUrlMatchesScheme(serviceData) || super.accept(serviceData);
	}

	protected boolean jdbcUrlMatchesScheme(Map<String, Object> serviceData) {
		if (getUriSchemes() == null) {
			return false;
		}

		Map<String, Object> credentials = getCredentials(serviceData);
		String jdbcUrl = getStringFromCredentials(credentials, "jdbcUrl");

		if (jdbcUrl != null) {
			for (String uriScheme : getUriSchemes()) {
				if (jdbcUrl.startsWith(JDBC_PREFIX + uriScheme + ":")) {
					return true;
				}
			}
		}

		return false;
	}

	public abstract SI createServiceInfo(String id, String uri, String jdbcUrl);

	public SI createServiceInfo(Map<String, Object> serviceData) {
		String id = getId(serviceData);

		Map<String,Object> credentials = getCredentials(serviceData);

		String jdbcUrl = getStringFromCredentials(credentials, "jdbcUrl");

		String uri = getUriFromCredentials(credentials);

		if (uri == null) {
			String host = getStringFromCredentials(credentials, "hostname", "host");
			int port = getIntFromCredentials(credentials, "port");

			String username = getStringFromCredentials(credentials, "user", "username");
			String password = (String) credentials.get("password");

			String database = (String) credentials.get("name");

			if (host != null) {
				uri = new UriInfo(getDefaultUriScheme(), host, port, username, password, database).toString();
			}
		}

		if (uri == null) {
			uri = jdbcUrl;
		}

		return createServiceInfo(id, uri, jdbcUrl);
	}
}
