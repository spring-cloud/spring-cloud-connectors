package org.springframework.cloud.util;

/**
 * An interface implemented in order to create {@link UriInfo}s.
 * 
 * If your {@link org.springframework.cloud.service.ServiceInfo} needs to deal with special URIs and you are extending
 * {@link org.springframework.cloud.service.UriBasedServiceInfo} then you can implement this factory interface in order
 * to return a custom factory from your ServiceInfo by overriding
 * {@link org.springframework.cloud.service.UriBasedServiceInfo#getUriInfoFactory()}.
 * 
 * @author Jens Deppe
 */
public interface UriInfoFactory {

    /**
     * Create a {@link UriInfo} based on a URI string
     * @param uriString the URI string to parse
     * @return a {@link UriInfo}
     */
    public UriInfo createUri(String uriString);

    /**
     * Create a {@link UriInfo} based on explicit components of the URI
     * @param scheme
     * @param host
     * @param port
     * @param username
     * @param password
     * @param path
     * @return a {@link UriInfo}
     */
    public UriInfo createUri(String scheme, String host, int port, String username, String password, String path);
}
