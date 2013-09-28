package org.springframework.cloud;

import org.springframework.cloud.service.ServiceInfo;

/**
 * Creator of SerivceInfo objects specific to each cloud.
 * 
 * <p>
 * NOTE: While it will be convenient for {@link CloudConnector} implementations
 * to use this interface along with {@link AbstractCloudConnector} to support extensible
 * service model, they are free to use any other scheme.
 * 
 * @author Ramnivas Laddad
 * 
 */
public interface ServiceInfoCreator<SI extends ServiceInfo> {
	public boolean accept(Object serviceData);

	public SI createServiceInfo(Object serviceData);
}
