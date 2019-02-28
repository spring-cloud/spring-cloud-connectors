package org.springframework.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.util.ServiceLoaderWithExceptionControl;

import static java.lang.String.format;

/**
 * Factory for the cloud.
 *
 * <p>
 * The main entry point for user code. Typical code directly using this class (as opposed to using a dependency-injection framework)
 * will look like:
 *
 * <pre>
 * CloudFactory cloudFactory = new CloudFactory();
 * Cloud cloud = cloudFoundry.getCloud();
 * </pre>
 *
 * Creating object of this class is somewhat expensive (due to scanning of various files to load connectors), so consider caching
 * such an object if you create it manually. If you use a dependency-injection framework such as Spring, simply creating a bean for
 * either CloudFactory or using this class as a factory-bean for {@link Cloud} will suffice.
 *
 * @author Ramnivas Laddad
 *
 */
public class CloudFactory {
	private List<CloudConnector> cloudConnectors = new ArrayList<CloudConnector>();
	private List<ServiceConnectorCreator<?, ? extends ServiceInfo>> serviceCreators = new ArrayList<ServiceConnectorCreator<?, ? extends ServiceInfo>>();

	public CloudFactory() {
		scanCloudConnectors();
		scanServiceConnectorCreators();
	}

	/**
	 *
	 * @return a cloud suitable for the current environment
	 * @throws CloudException
	 *             if no suitable cloud found
	 */
	public Cloud getCloud() {
		CloudConnector suitableCloudConnector = null;
		for (CloudConnector cloudConnector : cloudConnectors) {
			if (cloudConnector.isInMatchingCloud()) {
				suitableCloudConnector = cloudConnector;
				break;
			}
		}

		if (suitableCloudConnector == null) {
			throw new CloudException("No suitable cloud connector found");
		}

		return new Cloud(suitableCloudConnector, serviceCreators);
	}

	/**
	 * Register a cloud connector.
	 *
	 * <p>
	 * CloudConnector developers should prefer the declarative mechanism described in README.MD instead of calling this method.
	 * </p>
	 *
	 * @param cloudConnector
	 *            the cloud connector to register for discovery
	 */
	public void registerCloudConnector(CloudConnector cloudConnector) {
		cloudConnectors.add(cloudConnector);
	}

	/* package access for testing */
	List<CloudConnector> getCloudConnectors() {
		return cloudConnectors;
	}

	/* package access for testing */
	List<ServiceConnectorCreator<?, ? extends ServiceInfo>> getServiceCreators() {
		return serviceCreators;
	}

	private void registerServiceCreator(ServiceConnectorCreator<?, ? extends ServiceInfo> serviceConnectorCreator) {
		serviceCreators.add(serviceConnectorCreator);
	}

	private void scanCloudConnectors() {
		Iterable<CloudConnector> loader = ServiceLoader.load(CloudConnector.class);
		for (CloudConnector cloudConnector : loader) {
			registerCloudConnector(cloudConnector);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void scanServiceConnectorCreators() {
		Iterable<ServiceConnectorCreator> loader = ServiceLoaderWithExceptionControl.load(ServiceConnectorCreator.class);
		for (ServiceConnectorCreator serviceConnectorCreator : loader) {
			if (serviceConnectorCreator != null) {
				registerServiceCreator(serviceConnectorCreator);
			}
		}
	}
}
