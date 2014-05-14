package org.springframework.cloud.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base class to simplify creation of {@link ServiceConnectorCreator}s. 
 * <p>
 * The type parameter define the service connector and service info types.
 * 
 * @param <SC> service connector type
 * @param <SI> service info type
 * 
 * @author Ramnivas Laddad
 */
public abstract class AbstractServiceConnectorCreator<SC, SI extends ServiceInfo>
		implements ServiceConnectorCreator<SC, SI> {

	private Class<SC> connectorType;
	private Class<SI> serviceInfoType;

	@SuppressWarnings("unchecked")
	public AbstractServiceConnectorCreator() {
		// A simple scheme to determine type parameters. If a type parameter in the class hierarchy
		// is of ServiceInfo type, we set the serrviceInfoType with it; other type parameters
		// are assumed to be of connector type. We let the last parameters override anything set
		// earlier to let the type parameters for classes close to AbstractServiceConnectorCreator win. 
		Class<?> clazz = getClass();
		while(clazz != AbstractServiceConnectorCreator.class) {
			if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
				ParameterizedType genericSuperClass = (ParameterizedType) clazz.getGenericSuperclass();
				Type[] typeArgs = genericSuperClass.getActualTypeArguments();
				for (Type typeArg : typeArgs) {
					if (typeArg instanceof Class) {
						Class<?> typeClass = (Class<?>) typeArg;
						if (ServiceInfo.class.isAssignableFrom(typeClass)) {
							this.serviceInfoType = (Class<SI>) typeClass;
						} else {
							this.connectorType = (Class<SC>) typeClass;
						}
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	public Class<SC> getServiceConnectorType() {
		return connectorType;
	}

	public Class<SI> getServiceInfoType() {
		return serviceInfoType;
	}
}
