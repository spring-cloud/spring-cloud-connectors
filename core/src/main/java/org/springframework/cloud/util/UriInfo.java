package org.springframework.cloud.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

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
	
	public UriInfo(String uriString) throws IllegalArgumentException {
		try {
			this.uri = new URI(uriString);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}

		scheme = uri.getScheme();
		host = uri.getHost();
		port = uri.getPort();

		String userInfo = uri.getRawUserInfo();
		if (userInfo != null) {
			String userPass[] = userInfo.split(":");
			if (userPass.length != 2) {
				throw new IllegalArgumentException("bad user info in URI: " + uri);
			}

			userName = uriDecode(userPass[0]);
			password = uriDecode(userPass[1]);
		} 

		String rawPath = uri.getRawPath();
		if (rawPath != null && rawPath.length() > 1) {
			path = rawPath.substring(1);
		} else {
			path = null;
		}
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

	private URI buildUri() throws IllegalArgumentException {
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
	
	@Override
	public String toString() {
		return uri.toString();
	}
}
