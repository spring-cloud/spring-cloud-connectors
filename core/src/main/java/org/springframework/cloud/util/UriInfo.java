package org.springframework.cloud.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class that allows expressing URIs in alternative forms: individual fields or a URI string
 * 
 * @author Ramnivas Laddad
 *
 */
public class UriInfo {
	private String scheme;
	private String host;
	private int port;
	private String userName;
	private String password;
	private String path;
	private URI uri;

	public UriInfo(String scheme, String host, int port, String username, String password, String path) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.userName = username;
		this.password = password;
		this.path = path;

		this.uri = buildUri();
	}

	public UriInfo(String scheme, String host, int port, String username, String password) {
		this(scheme, host, port, username, password, "");
	}

	public String getScheme() {
		return scheme;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getPath() {
		return path;
	}

	public URI getUri() {
		return uri;
	}

	public URI buildUri() throws IllegalArgumentException {
		String userInfo = null;
		
		if (userName != null && password != null) {
			userInfo = userName + ":" + password;
		}
		
		String cleanedPath = path == null || path.startsWith("/") ? path : "/" + path;
		
		try {
			return new URI(scheme, userInfo, host, port, cleanedPath, null, null);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String toString() {
		return uri.toString();
	}
}
