package org.springframework.cloud.config.xml;

import org.springframework.cloud.service.CloudServiceConnectorFactory;

/**
 * @author Thomas Risberg
 */
abstract class AbstractNestedElementCloudServiceFactoryParser extends AbstractCloudServiceFactoryParser {

	public AbstractNestedElementCloudServiceFactoryParser(Class<? extends CloudServiceConnectorFactory<?>> serviceConnectorFactoryType) {
		super(serviceConnectorFactoryType);
	}

}
