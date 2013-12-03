package org.springframework.cloud.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Factory for standard Cloud Foundry URIs which all conform to the format:
 *
 *   [jdbc:]scheme://[user:pass]@authority/path
 */
public class StandardUriInfoFactory implements UriInfoFactory {
  @Override
  public UriInfo createUri(String uriString) {
    String userName = null;
    String password = null;
    String path;
    URI tmpUri;

    if (uriString.startsWith("jdbc:")) {
      int idx = uriString.indexOf(":");
      uriString = uriString.substring(idx + 1);
    }

    try {
      tmpUri = new URI(uriString);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }

    String userInfo = tmpUri.getRawUserInfo();
    if (userInfo != null) {
      String userPass[] = userInfo.split(":");
      if (userPass.length != 2) {
        throw new IllegalArgumentException("bad user info in URI: " + tmpUri);
      }

      userName = uriDecode(userPass[0]);
      password = uriDecode(userPass[1]);
    }

    String rawPath = tmpUri.getRawPath();
    if (rawPath != null && rawPath.length() > 1) {
      path = rawPath.substring(1);
    } else {
      path = null;
    }
    return new UriInfo(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(), userName, password, path);
  }

  @Override
  public UriInfo createUri(String scheme, String host, int port,
      String username, String password, String path) {
    return new UriInfo(scheme, host, port, username, password, path);
  }

  private static String uriDecode(String s) {
    try {
      // URLDecode decodes '+' to a space, as for
      // form encoding.  So protect plus signs.
      return URLDecoder.decode(s.replace("+", "%2B"), "US-ASCII");
    } catch (java.io.UnsupportedEncodingException e) {
      // US-ASCII is always supported
      throw new RuntimeException(e);
    }
  }
}
