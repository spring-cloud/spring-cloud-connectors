package org.springframework.cloud.heroku;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.springframework.cloud.CloudException;

/**
 * Environment available to the deployed app.
 * 
 * @author Ramnivas Laddad
 */
public class EnvironmentAccessor {
	
	public Map<String, String> getEnv() {
		return System.getenv();
	}
	
	public String getValue(String key) {
		return System.getenv(key);
	}
	
	public String getPropertyValue(String key) {
		return System.getProperty(key);
	}

	public String getHost() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
			throw new CloudException(ex);
		}
	}
}
