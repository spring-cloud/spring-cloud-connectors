package org.springframework.cloud;

import java.util.List;

import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.service.ServiceInfo;

/**
 * Provider interface for cloud connector.
 * 
 * <p>
 * NOTE: This interface is intended for cloud providers. Users aren't expected to use it directly 
 * (instead use {@link Cloud} obtained through {@link CloudFactory} class) 
 * <p>
 * 
 * @author Ramnivas Laddad
 *
 */
public interface CloudConnector {

	/**
	 * Is the connector operating in its matching cloud? 
	 * Classes such as {@link CloudFactory} may use this method to select an appropriate connector.  
	 * 
	 * @return true if this connector is in its matching cloud
	 */
	boolean isInMatchingCloud();
	
	/**
	 * Get information about the application instance.
	 * 
	 * <p>
	 * This method cannot return <code>null</code>, since that would imply there is no application instance.
	 * 
	 * @return info
	 */
	ApplicationInstanceInfo getApplicationInstanceInfo();

	/**
	 * Get {@link ServiceInfo}s for services bound to the app
	 * 
	 * @return list of info (possibly empty list if no services are bound to the app)
	 */
	List<ServiceInfo> getServiceInfos();
}
