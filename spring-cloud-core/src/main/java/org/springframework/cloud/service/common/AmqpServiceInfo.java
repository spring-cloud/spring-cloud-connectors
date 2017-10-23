package org.springframework.cloud.service.common;

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.util.UriInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Information to access an AMQP service.
 *
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
@ServiceLabel("rabbitmq")
public class AmqpServiceInfo extends UriBasedServiceInfo {
	private static final Integer DEFAULT_AMQP_PORT = 5672;
	private static final Integer DEFAULT_AMQPS_PORT = 5671;

	public static final String AMQP_SCHEME = "amqp";
	public static final String AMQPS_SCHEME = "amqps";

	private String managementUri;

	private List<String> uris;
	private List<String> managementUris;

	private String virtualHost;

	public AmqpServiceInfo(String id, String host, int port, String username, String password, String virtualHost) {
		this(id, host, port, username, password, virtualHost, null);
	}

	public AmqpServiceInfo(String id, String host, int port, String username, String password, String virtualHost, String managementUri) {
		super(id, AMQP_SCHEME, host, port, username, password, virtualHost);
		this.managementUri = managementUri;
		this.virtualHost = decode(getUriInfo().getPath());
	}

	public AmqpServiceInfo(String id, String uri, String managementUri, List<String> uris, List<String> managementUris) {
		this(id, uri, managementUri);
		this.uris = uris;
		this.managementUris = managementUris;
	}

	public AmqpServiceInfo(String id, String uri) throws CloudException {
		this(id, uri, null);
	}

	public AmqpServiceInfo(String id, String uri, String managementUri) throws CloudException {
		super(id, uri);
		this.managementUri = managementUri;
		this.virtualHost = decode(getUriInfo().getPath());
	}

	private String decode(String value) {
		if (value == null) {
			return null;
		}

		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	@ServiceProperty(category="connection")
	public String getVirtualHost() {
		return virtualHost;
	}

	@ServiceProperty(category="connection")
	public String getManagementUri() {
		return managementUri;
	}

	@ServiceProperty(category="connection")
	public List<String> getUris() {
		return uris;
	}

	@ServiceProperty(category="connection")
	public List<String> getManagementUris() {
		return managementUris;
	}

	@Override
	public int getPort() {
		if (super.getPort() == -1) {
			if (getScheme().equals(AMQP_SCHEME)) {
				return DEFAULT_AMQP_PORT;
			} else if (getScheme().equals(AMQPS_SCHEME)) {
				return DEFAULT_AMQPS_PORT;
			}
		}
		return super.getPort();
	}

	@Override
	protected UriInfo validateAndCleanUriInfo(UriInfo uriInfo) {
		if (uriInfo.getScheme() == null) {
			throw new IllegalArgumentException("Missing scheme in amqp URI: " + uriInfo);
		}

		if (uriInfo.getHost() == null) {
			throw new IllegalArgumentException("Missing authority in amqp URI: " + uriInfo);
		}

		if (uriInfo.getUserName() == null || uriInfo.getPassword() == null) {
			throw new IllegalArgumentException("Missing userinfo in amqp URI: " + uriInfo);
		}

		String path = uriInfo.getPath();
		if (path != null) {
			// Check that the path only has a single segment.  As we have an authority component
			// in the URI, paths always begin with a slash.
			if (path.indexOf('/') != -1) {
				throw new IllegalArgumentException("Multiple segments in path of amqp URI: " + uriInfo);
			}
		}

		return uriInfo;
	}
}
