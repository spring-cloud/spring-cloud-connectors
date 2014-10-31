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
		// the literal in the tag is CloudFoundry-specific
		super(new Tags("smtp"), SmtpServiceInfo.SMTP_SCHEME);
	}

	public SmtpServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String,Object> credentials = getCredentials(serviceData);
		String host = (String) credentials.get("hostname");

		int port = getIntFromCredentials(credentials, "port");
		if (port == -1) {
			port = DEFAULT_SMTP_PORT;
		}

		String username = (String) credentials.get("username");
		String password = (String) credentials.get("password");

		String uri = new UriInfo(SmtpServiceInfo.SMTP_SCHEME, host, port, username, password).toString();

		return new SmtpServiceInfo(id, uri);
	}

}
