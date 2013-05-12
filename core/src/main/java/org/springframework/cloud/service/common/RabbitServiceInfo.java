package org.springframework.cloud.service.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * Information to access RabbitMQ service.
 *
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("rabbitmq")
public class RabbitServiceInfo extends BaseServiceInfo {
	private String virtualHost;

	public RabbitServiceInfo(String id, String uri)	throws CloudException {
		super(id);
		try {
			parseAmqpUri(uri);
		} catch (URISyntaxException e) {
			throw new CloudException(e);
		}
	}
	
	public RabbitServiceInfo(String id, String host, int port, String userName, String password, String virtualHost) {
		super(id, host, port, userName, password);
		
		this.virtualHost = virtualHost;
	}

	@ServiceProperty(category="connection")
	public String getVirtualHost() {
		return virtualHost;
	}

	private void parseAmqpUri(String uristr) throws URISyntaxException {
		URI uri = new URI(uristr);

		if (!"amqp".equals(uri.getScheme())) {
			throw new IllegalArgumentException("wrong scheme in amqp URI: " + uristr);
		}

		host = uri.getHost();
		if (host == null) {
			throw new IllegalArgumentException("missing authority in amqp URI: " + uristr);
		}
		
		port = uri.getPort();
		if (port == -1) {
			port = 5672;
		}

		String userInfo = uri.getRawUserInfo();
		if (userInfo != null) {
			String userPass[] = userInfo.split(":");
			if (userPass.length != 2) {
				throw new IllegalArgumentException("bad user info in amqp URI: " + uristr);
			}

			userName = uriDecode(userPass[0]);
			password = uriDecode(userPass[1]);
		} else {
			userName = "guest";
			password = "guest";
		}

		String path = uri.getRawPath();
		if (path == null || path.length() == 0) {
			// The RabbitMQ default vhost
			virtualHost = "/";
		}
		else {
			// Check that the path only has a single
			// segment.  As we have an authority component
			// in the URI, paths always begin with a
			// slash.
			if (path.indexOf('/', 1) != -1) {
				throw new IllegalArgumentException("multiple segemtns in path of amqp URI: " + uristr);
			}
			virtualHost = uri.getPath().substring(1);
		}
	}

	private String uriDecode(String s) {
		try {
			// URLDecode decodes '+' to a space, as for
			// form encoding.  So protect plus signs.
			return URLDecoder.decode(s.replace("+", "%2B"),	 "US-ASCII");
		} catch (java.io.UnsupportedEncodingException e) {
			// US-ASCII is always supported
			throw new RuntimeException(e);
		}
	}

}
