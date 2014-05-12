package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.SmtpServiceInfo;
import org.springframework.cloud.util.UriInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class SmtpServiceInfoCreator extends CloudFoundryServiceInfoCreator<SmtpServiceInfo> {

	private static final int DEFAULT_SMTP_PORT = 587;

	public SmtpServiceInfoCreator() {
		super("smtp", "smtp");
	}

	public SmtpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		String id = (String) serviceData.get("name");
		
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		String host = (String) credentials.get("hostname");
		
		int port = DEFAULT_SMTP_PORT;
		if (credentials.containsKey("port")) {
			port = Integer.parseInt(credentials.get("port").toString());
		}

		String username = (String) credentials.get("username");
		String password = (String) credentials.get("password");

		String uri = new UriInfo("smtp", host, port, username, password).toString();
		
		return new SmtpServiceInfo(id, uri);
	}

}
