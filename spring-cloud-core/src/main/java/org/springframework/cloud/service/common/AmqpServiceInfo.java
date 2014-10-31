package org.springframework.cloud.service.common;

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.util.UriInfo;

/**
 * Information to access RabbitMQ service.
 *
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("rabbitmq")
public class AmqpServiceInfo extends UriBasedServiceInfo {

    public static final String URI_SCHEME = "amqp";

	public AmqpServiceInfo(String id, String host, int port, String username, String password, String virtualHost) {
		super(id, URI_SCHEME, host, port, username, password, virtualHost);
	}

	public AmqpServiceInfo(String id, String uri)	throws CloudException {
		super(id, uri);
	}

	@ServiceProperty(category="connection")
	public String getVirtualHost() {
		return getUriInfo().getPath();
	}

	@Override
	protected UriInfo validateAndCleanUriInfo(UriInfo uriInfo) {
		if (uriInfo.getScheme() == null) {
			throw new IllegalArgumentException("missing scheme in amqp URI: " + uriInfo);
		}

		if (uriInfo.getHost() == null) {
			throw new IllegalArgumentException("missing authority in amqp URI: " + uriInfo);
		}

		int port = uriInfo.getPort();
		if (port == -1) {
			port = 5672;
		}

		String userName = uriInfo.getUserName();
		String password = uriInfo.getPassword();

		if (userName == null || password == null) {
			throw new IllegalArgumentException("missing userinfo in amqp URI: " + uriInfo);
		}

		String path = uriInfo.getPath();
		if (path == null) {
			// The RabbitMQ default vhost
			path = "/";
		} else {
			// Check that the path only has a single segment.  As we have an authority component
			// in the URI, paths always begin with a slash.
			if (path.indexOf('/') != -1) {
				throw new IllegalArgumentException("multiple segments in path of amqp URI: " + uriInfo);
			}
		}
		return new UriInfo(uriInfo.getScheme(), uriInfo.getHost(), port, uriInfo.getUserName(), uriInfo.getPassword(), path);
	}
}
