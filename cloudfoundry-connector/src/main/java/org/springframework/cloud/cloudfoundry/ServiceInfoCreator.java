package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.ServiceInfo;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public interface ServiceInfoCreator<T extends ServiceInfo> {
	public boolean accept(Object serviceData);
	
	public T createServiceInfo(Object serviceData);
}
