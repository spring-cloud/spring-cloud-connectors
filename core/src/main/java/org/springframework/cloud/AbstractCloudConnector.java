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
public abstract class AbstractCloudConnector<SD> implements CloudConnector {

	protected List<ServiceInfoCreator<?>> serviceInfoCreators = new ArrayList<ServiceInfoCreator<?>>();

	abstract protected List<SD> getServicesData();
	
	public AbstractCloudConnector(Class<? extends ServiceInfoCreator<? extends ServiceInfo>> serviceInfoCreatorClass) {
		scanServiceInfoCreators(serviceInfoCreatorClass);
	}

	@Override
	public List<ServiceInfo> getServiceInfos() {
		List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
		for (SD serviceData : getServicesData()) {
			serviceInfos.add(getServiceInfo(serviceData));
		}
		
		return serviceInfos;
	}

	private void registerServiceInfoCreator(ServiceInfoCreator<?> serviceInfoCreator) {
		serviceInfoCreators.add(serviceInfoCreator);
	}

	private void scanServiceInfoCreators(Class<? extends ServiceInfoCreator<?>> serviceInfoCreatorClass) {
		@SuppressWarnings("rawtypes")
		ServiceLoader<? extends ServiceInfoCreator> serviceInfoCreators = ServiceLoader.load(serviceInfoCreatorClass);
		for (ServiceInfoCreator<?> serviceInfoCreator : serviceInfoCreators) {
			registerServiceInfoCreator(serviceInfoCreator);
		}
	}
	
	private ServiceInfo getServiceInfo(SD serviceData) {
		for (ServiceInfoCreator<?> serviceInfoCreator : serviceInfoCreators) {
			if (serviceInfoCreator.accept(serviceData)) {
				return serviceInfoCreator.createServiceInfo(serviceData);
			}
		}
		
		throw new CloudException("No suitable service info creator found");
	}
}