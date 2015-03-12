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
	private String query;
	private String rawUriString;

	public UriInfo(String scheme, String host, int port, String username, String password) {
		this(scheme, host, port, username, password,
				String.format("%s://%s:%s@%s:%s/", scheme, username, password, host, port));
	}

	public UriInfo(String scheme, String host, int port, String username, String password, String path) {
		this(scheme, host, port, username, password, path, null,
				String.format("%s://%s:%s@%s:%s/%s", scheme, username, password, host, port, path));
	}

	public UriInfo(String scheme, String host, int port, String username, String password, String path,
				   String query, String rawUriString) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.userName = username;
		this.password = password;
		this.path = path;
		this.query = query;
		this.rawUriString = rawUriString;

		this.uri = buildUri();
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

	public String getQuery() {
		return query;
	}

	public URI getUri() {
		return uri;
	}

	public String getRawUriString() {
		return rawUriString;
	}

	private URI buildUri() {
		String userInfo = null;
		
		if (userName != null && password != null) {
			userInfo = userName + ":" + password;
		}
		
		String cleanedPath = path == null || path.startsWith("/") ? path : "/" + path;
		
		try {
			return new URI(scheme, userInfo, host, port, cleanedPath, query, null);
		}
		catch (URISyntaxException e) {
			String details = String.format("Error creating URI with components: " +
							"scheme=%s, userInfo=%s, host=%s, port=%d, path=%s, query=%s",
					scheme, userInfo, host, port, cleanedPath, query);
			throw new IllegalArgumentException(details, e);
		}
	}

	@Override
	public String toString() {
		return uri.toString();
	}
}
