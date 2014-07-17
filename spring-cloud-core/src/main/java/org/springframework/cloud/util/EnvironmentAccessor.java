package org.springframework.cloud.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.springframework.cloud.CloudConnector;
import org.springframework.cloud.CloudException;

/**
 * Environment available to the deployed app.
 *
 * The main purpose of this class is to allow unit-testing of {@link CloudConnector} implementations
 * that rely on environment
 *
 * @author Ramnivas Laddad
 */
public class EnvironmentAccessor {

	public Map<String, String> getEnv() {
		return System.getenv();
	}

	public String getEnvValue(String key) {
		return System.getenv(key);
	}

	public Properties getSystemProperties() {
	    return System.getProperties();
	}

	public String getSystemProperty(String key) {
		return getSystemProperty(key, null);
	}

	public String getSystemProperty(String key, String def) {
	    return System.getProperty(key, def);
	}

	public String getHost() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
			throw new CloudException(ex);
		}
	}
}
