package org.springframework.cloud.service;

import org.springframework.cloud.util.StandardUriInfoFactory;
import org.springframework.cloud.util.UriInfo;
import org.springframework.cloud.util.UriInfoFactory;

/**
 * Common class for all {@link ServiceInfo}s
 *
 * @author Ramnivas Laddad
 *
 */
public abstract class UriBasedServiceInfo extends BaseServiceInfo {
	private UriInfo uriInfo;

	private static UriInfoFactory uriFactory = new StandardUriInfoFactory();

	public UriBasedServiceInfo(String id, String scheme, String host, int port, String username, String password, String path) {
		super(id);
		this.uriInfo = getUriInfoFactory().createUri(scheme, host, port, username, password, path);
		this.uriInfo = validateAndCleanUriInfo(uriInfo);
	}

	public UriBasedServiceInfo(String id, String uriString) {
		super(id);
		this.uriInfo = getUriInfoFactory().createUri(uriString);
		this.uriInfo = validateAndCleanUriInfo(uriInfo);
	}

	/**
	 * For URI-based (@link ServiceInfo}s which don't conform to the standard URI
	 * format, override this method in your own ServiceInfo class to return a {@link UriInfoFactory} which will create the
	 * appropriate URIs.
	 *
	 * @return your special UriInfoFactory
	 */
	public UriInfoFactory getUriInfoFactory() {
		return uriFactory;
	}

	@ServiceProperty(category = "connection")
	public String getUri() {
		return uriInfo.getUriString();
	}

	@ServiceProperty(category = "connection")
	public String getUserName() {
		return uriInfo.getUserName();
	}

	@ServiceProperty(category = "connection")
	public String getPassword() {
		return uriInfo.getPassword();
	}

	@ServiceProperty(category = "connection")
	public String getHost() {
		return uriInfo.getHost();
	}

	@ServiceProperty(category = "connection")
	public int getPort() {
		return uriInfo.getPort();
	}

	@ServiceProperty(category = "connection")
	public String getPath() {
		return uriInfo.getPath();
	}

	@ServiceProperty(category = "connection")
	public String getQuery() {
		return uriInfo.getQuery();
	}

	@ServiceProperty(category = "connection")
	public String getScheme() {
		return uriInfo.getScheme();
	}

	@ServiceProperty(category = "connection")
	public String getSchemeSpecificPart() {
		return uriInfo.getSchemeSpecificPart();
	}

	/**
	 * Validate the URI and clean it up by using defaults for any missing information, if possible.
	 *
	 * @param uriInfo
	 *            uri info based on parsed payload
	 * @return cleaned up uri info
	 */
	protected UriInfo validateAndCleanUriInfo(UriInfo uriInfo) {
		return uriInfo;
	}

	protected UriInfo getUriInfo() {
		return uriInfo;
	}

	@Override
	public String toString() {
		// TODO: when using a simple URI string (see comments in getUri), the result of uriInfo.getUriString()
		//       would display the password which does not seem ideal.
		return getClass().getSimpleName() + "[" + getScheme() + "://" + getUserName() + ":****@" + getHost() + ":" + getPort()
			+ "/" + getPath() + "]";
	}
}
