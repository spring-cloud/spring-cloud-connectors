package org.springframework.cloud.service.common;

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.util.UriInfo;

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

	public static final String AMQP_SCHEME = "amqp";
	public static final String AMQPS_SCHEME = "amqps";

	private String managementUri;

	private List<String> uris;
	private List<String> managementUris;

	public AmqpServiceInfo(String id, String host, int port, String username, String password, String virtualHost) {
		this(id, host, port, username, password, virtualHost, null);
	}

	public AmqpServiceInfo(String id, String host, int port, String username, String password, String virtualHost, String managementUri) {
		super(id, AMQP_SCHEME, host, port, username, password, virtualHost);
		this.managementUri = managementUri;
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
	}

	@ServiceProperty(category="connection")
	public String getVirtualHost() {
		return getUriInfo().getPath();
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
