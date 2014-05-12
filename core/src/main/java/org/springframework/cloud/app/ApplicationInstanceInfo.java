package org.springframework.cloud.app;

import java.util.Map;

/**
 * Information about the application instance.
 * 
 * Except for the instance and application id, everything else is loosely
 * defined as a map holding properties to allow each cloud to define in
 * whatever way it suits them.
 *   
 * @author Ramnivas Laddad
 *
 */
public interface ApplicationInstanceInfo {
	/**
	 * Id for this particular instance
	 * 
	 * @return typically some unique id or instance index
	 * 
	 */
	public String getInstanceId();
	
	/**
	 * Id for the app
	 * 
	 * @return typically the name of the application
	 */
	public String getAppId();
	
	/**
	 * Loosely defined map of application and instance properties.
	 * 
	 * <p>
	 * Typical properties could include hostname and port etc.
	 * 
	 * @return map of properties
	 */
	public Map<String, Object> getProperties();
}
