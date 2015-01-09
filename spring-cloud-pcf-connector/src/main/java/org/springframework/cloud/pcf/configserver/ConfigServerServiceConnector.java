package org.springframework.cloud.pcf.configserver;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Connector to Config Server service
 *
 * @author Chris Schaefer
 */
@Configuration
public class ConfigServerServiceConnector implements ApplicationListener<ApplicationEvent>, Ordered {
	/**
	 * TODO:
	 * Bind ApplicationListener to ApplicationEnvironmentPreparedEvent, remove reflection
	 * and add test after repo migration in which we will have a direct dependency on boot.
	 */

	private static final String PROPERTY_SOURCE_NAME = "vcapConfigServerUri";
	private static final String EVENT_ENVIRONMENT_METHOD_NAME = "getEnvironment";
	private static final String SPRING_CLOUD_CONFIG_URI = "spring.cloud.config.uri";
	private static final String EVENT_CLASS_NAME = "org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent";

	private Cloud cloud;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(!supports(event.getClass().getName()) || cloud != null) {
			return;
		}

		cloud = new CloudFactory().getCloud();

		for(ServiceInfo serviceInfo : cloud.getServiceInfos()) {
			if(serviceInfo instanceof ConfigServerServiceInfo) {
				String uri = ((ConfigServerServiceInfo) serviceInfo).getUri();

				MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME,
						Collections.<String, Object>singletonMap(SPRING_CLOUD_CONFIG_URI, uri));

				getEnvironment(event).getPropertySources().addFirst(mapPropertySource);
			}
		}
	}

	private boolean supports(String className) {
		return EVENT_CLASS_NAME.equals(className);
	}

	private ConfigurableEnvironment getEnvironment(ApplicationEvent event) {
		Method method = ReflectionUtils.findMethod(event.getClass(), EVENT_ENVIRONMENT_METHOD_NAME);

		try {
			return (ConfigurableEnvironment) method.invoke(event);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining Environment from event", e);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}
}
