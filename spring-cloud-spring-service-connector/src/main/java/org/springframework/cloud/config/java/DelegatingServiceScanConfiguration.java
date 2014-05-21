package org.springframework.cloud.config.java;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.CloudServicesScanner;
import org.springframework.cloud.service.GenericCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * The class that introduces a {@link CloudServicesScanner} bean to scan
 * services
 * 
 * @author Ramnivas Laddad
 * @see ServiceScan
 */
public class DelegatingServiceScanConfiguration implements
		ImportBeanDefinitionRegistrar {

	private static Log logger = LogFactory
			.getLog(DelegatingServiceScanConfiguration.class);

	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		CloudFactory factory = new CloudFactory();
		if (registry instanceof BeanFactory) {
			try {
				factory = ((BeanFactory) registry).getBean(CloudFactory.class);
			} catch (BeansException e) {
				// ignore
			}
		}
		logger.info("Registering cloud services");
		registerServiceBeans(factory.getCloud(), registry);
	}

	private void registerServiceBeans(Cloud cloud,
			BeanDefinitionRegistry registry) {
		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();

		for (ServiceInfo serviceInfo : serviceInfos) {
			registerServiceBean(registry, serviceInfo,
					cloud.getServiceConnector(serviceInfo.getId(), null, null));
		}
	}

	private void registerServiceBean(BeanDefinitionRegistry registry,
			ServiceInfo serviceInfo, Object service) {
		try {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder
					.genericBeanDefinition(GenericCloudServiceConnectorFactory.class);
			builder.addConstructorArgValue(serviceInfo.getId());
			builder.addConstructorArgValue(null);
			builder.getRawBeanDefinition().setAttribute(
					"factoryBeanObjectType", service.getClass());
			registry.registerBeanDefinition(serviceInfo.getId(),
					builder.getBeanDefinition());
			logger.info("Registered service bean for: " + serviceInfo);
		} catch (Exception ex) {
			throw new CloudException("Error registering service factory", ex);
		}

	}
}
