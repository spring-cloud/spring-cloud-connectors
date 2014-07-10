package org.springframework.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

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

	private static Logger logger = Logger.getLogger(AbstractCloudConnector.class.getName());

	protected List<ServiceInfoCreator<?,SD>> serviceInfoCreators = new ArrayList<ServiceInfoCreator<?,SD>>();

	protected abstract List<SD> getServicesData();
	protected abstract FallbackServiceInfoCreator<?,SD> getFallbackServiceInfoCreator();

	public AbstractCloudConnector(Class<? extends ServiceInfoCreator<? extends ServiceInfo, ?>> serviceInfoCreatorClass) {
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

	protected void registerServiceInfoCreator(ServiceInfoCreator<? extends ServiceInfo, SD> serviceInfoCreator) {
		serviceInfoCreators.add(serviceInfoCreator);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void scanServiceInfoCreators(Class<? extends ServiceInfoCreator<? extends ServiceInfo,?>> serviceInfoCreatorClass) {
		ServiceLoader<? extends ServiceInfoCreator> serviceInfoCreators = ServiceLoader.load(serviceInfoCreatorClass);
		for (ServiceInfoCreator<? extends ServiceInfo,SD> serviceInfoCreator : serviceInfoCreators) {
			registerServiceInfoCreator(serviceInfoCreator);
		}
	}

	private ServiceInfo getServiceInfo(SD serviceData) {
		for (ServiceInfoCreator<? extends ServiceInfo,SD> serviceInfoCreator : serviceInfoCreators) {
			if (serviceInfoCreator.accept(serviceData)) {
				return serviceInfoCreator.createServiceInfo(serviceData);
			}
		}

		// Fallback with a warning
		ServiceInfo fallackServiceInfo = getFallbackServiceInfoCreator().createServiceInfo(serviceData);
		logger.warning("No suitable service info creator found for service " + fallackServiceInfo.getId()
				+ " Did you forget to add a ServiceInfoCreator?");
		return fallackServiceInfo;
	}

    public static class KeyValuePair {
    	private final String key;
    	private final String value;

    	public KeyValuePair(String key, String value) {
    		this.key = key;
    		this.value = value;
    	}

    	public String getKey() {
    		return key;
    	}

    	public String getValue() {
    		return value;
    	}
    }
}