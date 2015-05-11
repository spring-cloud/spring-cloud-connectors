package org.springframework.cloud.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Factory for standard Cloud Foundry URIs, which all conform to the format:
 * <p>
 * {@code scheme://[user:pass]@authority[:port]/path}
 */
public class StandardUriInfoFactory implements UriInfoFactory {

	@Override
	public UriInfo createUri(String scheme, String host, int port, String username, String password, String path) {
		return new UriInfo(scheme, host, port, username, password, path);
	}

	@Override
	public UriInfo createUri(String uriString) {
		URI tmpUri = uriFromString(uriString);

		String[] userInfo = parseUserinfo(tmpUri);
		String userName = uriDecode(userInfo[0]);
		String password = uriDecode(userInfo[1]);

		return new UriInfo(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(),
				userName, password, parsePath(tmpUri), tmpUri.getRawQuery(), uriString);
	}

	private URI uriFromString(String uriString) {
		try {
			return new URI(uriString);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid URI " + uriString, e);
		}
	}

	private String[] parseUserinfo(URI uri) {
		String userInfo = uri.getRawUserInfo();

		if (userInfo != null) {
			String[] userPass = userInfo.split(":");
			if (userPass.length != 2) {
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
}
