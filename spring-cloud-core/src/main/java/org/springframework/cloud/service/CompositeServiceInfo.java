package org.springframework.cloud.service;

import java.util.List;

import org.springframework.cloud.Cloud;

/**
 * Interface to represent services constituted out of other services.
 * 
 * <p>
 * With services such as Pivotal HD, even though the app will be bound to one such service, 
 * from the application developer's point of view, it needs to access the constituent services.
 * This interface expresses such a service, which the {@link Cloud} class flattens into
 * its constituents. Then from the rest of the Spring Cloud infrastructure as well as application
 * code, the effect is as if all the constituents were individually bound.
 * 
 * @author Ramnivas Laddad
 *
 */
public interface CompositeServiceInfo extends ServiceInfo {

    List<ServiceInfo> getServiceInfos();
    
}
