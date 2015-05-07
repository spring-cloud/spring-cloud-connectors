package org.springframework.cloud.config.java;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.config.CloudScanHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Introduces beans for each bound service.
 * 
 * @author Ramnivas Laddad
 * @see ServiceScan
 */
@Configuration
public class ServiceScanConfiguration implements ImportBeanDefinitionRegistrar {
	private CloudScanHelper helper = new CloudScanHelper();

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) throws BeansException {
		helper.registerServiceBeans(registry);
	}
}
