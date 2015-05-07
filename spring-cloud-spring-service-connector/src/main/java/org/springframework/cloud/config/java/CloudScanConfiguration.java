package org.springframework.cloud.config.java;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.config.CloudScanHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Introduces beans for each bound service and one for {@link ApplicationInstanceInfo}
 * 
 * @author Ramnivas Laddad
 * @see CloudScan
 */
@Configuration
public class CloudScanConfiguration extends ServiceScanConfiguration {
	private CloudScanHelper helper = new CloudScanHelper();

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) throws BeansException {
		super.registerBeanDefinitions(importingClassMetadata, registry);
		helper.registerApplicationInstanceBean(registry);
	}
}
