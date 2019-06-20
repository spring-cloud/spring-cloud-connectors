package org.springframework.cloud.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utility class that allows expressing URIs in alternative forms: individual fields or a URI string
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 */
public class UriInfo {
	private String scheme;
	private String host;
	private int port;
	private String userName;
	private String password;
	private String path;
	private String query;
	private String schemeSpecificPart;

	private String uriString;

	public UriInfo(String scheme, String host, int port, String username, String password) {
		this(scheme, host, port, username, password, null, null);
	}

	public UriInfo(String scheme, String host, int port, String username, String password, String path) {
		this(scheme, host, port, username, password, path, null);
	}

	public UriInfo(String scheme, String host, int port, String username, String password, String path, String query) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.userName = username;
		this.password = password;
		this.path = path;
		this.query = query;

		this.uriString = buildUri().toString();
	}

	public UriInfo(String uriString) {
		this.uriString = uriString;

		URI uri = getUri();
		this.scheme = uri.getScheme();
		this.host = uri.getHost();
		this.port = uri.getPort();
		this.path = parsePath(uri);
		this.query = uri.getQuery();
		this.schemeSpecificPart = uri.getSchemeSpecificPart();

		String[] userinfo = parseUserinfo(uri);
		this.userName = uriDecode(userinfo[0]);
		this.password = uriDecode(userinfo[1]);
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

	public String getSchemeSpecificPart() {
		return schemeSpecificPart;
	}

	public URI getUri() {
		try {
			return new URI(uriString);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid URI " + uriString, e);
		}
	}

	public String getUriString() {
		return uriString;
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

	private String[] parseUserinfo(URI uri) {
		String userInfo = uri.getRawUserInfo();

		if (userInfo != null) {
			String[] userPass = userInfo.split(":");
			if (userPass.length == 1) {
				return new String[]{userPass[0], null};
			} else if (userPass.length != 2) {
				throw new IllegalArgumentException("Bad userinfo in URI: " + uri);
			}
			return userPass;
		}

		return new String[]{null, null};
	}

	private String parsePath(URI uri) {
		String rawPath = uri.getRawPath();
		if (rawPath != null && rawPath.length() > 1) {
			return rawPath.substring(1);
		}
		return null;
	}

	private static String uriDecode(String s) {
		if (s == null) {
			return null;
		}

		try {
			// URLDecode decodes '+' to a space, as for
			// form encoding. So protect plus signs.
			return URLDecoder.decode(s.replace("+", "%2B"), "US-ASCII");
		} catch (java.io.UnsupportedEncodingException e) {
			// US-ASCII is always supported
			throw new RuntimeException(e);
		}
	}
	
	public static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return uriString;
	}
}
