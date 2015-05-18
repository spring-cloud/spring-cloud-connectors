package org.springframework.cloud.util;

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
		return new UriInfo(uriString);
	}
}
