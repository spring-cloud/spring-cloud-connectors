package org.springframework.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class SmtpServiceInfo extends UriBasedServiceInfo {

	public SmtpServiceInfo(String id, String host, int port, String username, String password) {
		super(id, "smtp", host, port, username, password, "");
	}
	
	public SmtpServiceInfo(String id, String url) {
		super(id, url);
	}

}
