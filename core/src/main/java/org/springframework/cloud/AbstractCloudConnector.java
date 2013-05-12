package org.springframework.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.springframework.cloud.service.ServiceInfo;


/**
 * Helper abstract class to simplify {@link CloudConnector} implementations.
 * 
 * User the {@link ServiceLoader} approach to looks for file name matching the class passed in constructor
 * and registers {@link ServiceInfoCreator} found there.
 * 
 * Implementation of {@link CloudConnector}s that wish to support the recommended service scanning approach
 * should extends this approach to gain that functionality automatically.
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractCloudConnector implements CloudConnector {

	protected List<ServiceInfoCreator<?>> serviceInfoCreators = new ArrayList<ServiceInfoCreator<?>>();

	public AbstractCloudConnector(Class<? extends ServiceInfoCreator<? extends ServiceInfo>> serviceInfoCreatorClass) {
		scanServiceInfoCreators(serviceInfoCreatorClass);
	}

	public void registerServiceInfoCreator(ServiceInfoCreator<?> serviceInfoCreator) {
		serviceInfoCreators.add(serviceInfoCreator);
	}

	private void scanServiceInfoCreators(Class<? extends ServiceInfoCreator<?>> serviceInfoCreatorClass) {
		@SuppressWarnings("rawtypes")
		ServiceLoader<? extends ServiceInfoCreator> serviceInfoCreators = ServiceLoader.load(serviceInfoCreatorClass);
		for (ServiceInfoCreator<?> serviceInfoCreator : serviceInfoCreators) {
			registerServiceInfoCreator(serviceInfoCreator);
		}
	}
}