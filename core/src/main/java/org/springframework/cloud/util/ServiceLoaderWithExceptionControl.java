package org.springframework.cloud.util;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import org.springframework.cloud.service.ServiceConnectorCreator;

/**
 * A variation of {@link ServiceLoader} that ignores exception encountered when loading services.
 * 
 * <p>
 * The {@link ServiceLoader} class throws an error when it encounters a service that it cannot load.
 * For {@link ServiceConnectorCreator}, this can be a common occurrence due to missing dependencies.
 * For example, if an app isn't using MongoDB, thus not declaring dependencies on relevant classes,
 * will cause {@link ServiceConnectorCreator} for MongoDB to fail. We don't want to abandon other service
 * connector creators in such cases.
 * </p>
 * 
 * @author Ramnivas Laddad
 *
 * @param <T>
 */
public class ServiceLoaderWithExceptionControl<T> implements Iterable<T> {
	private Iterable<T> underlying;
	
	private static Logger logger = Logger.getLogger(ServiceLoaderWithExceptionControl.class.getName());

	public static <T> Iterable<T> load(Class<T> serviceType) {
		ServiceLoader<T> loader = ServiceLoader.load(serviceType);
		return new ServiceLoaderWithExceptionControl<T>(loader);
	}

	private ServiceLoaderWithExceptionControl(Iterable<T> underlying) {
		this.underlying = underlying;
	}

	@Override
	public Iterator<T> iterator() {
		return new ServiceLoaderIterator(underlying.iterator());
	}
	
	private class ServiceLoaderIterator implements Iterator<T> {
		private Iterator<T> underlying;
	
		public ServiceLoaderIterator(Iterator<T> underlying) {
			this.underlying = underlying;
		}
		
		@Override
		public boolean hasNext() {
			return underlying.hasNext();
		}
	
		@Override
		public T next() {
			try {
				return underlying.next();
			} catch (ServiceConfigurationError e) {
				logger.warning("Failed to load " + e);
				if (hasNext()) {
					return next();
				}
			}
			return null;
		}
	
		@Override
		public void remove() {
			underlying.remove();
		}
	}
}