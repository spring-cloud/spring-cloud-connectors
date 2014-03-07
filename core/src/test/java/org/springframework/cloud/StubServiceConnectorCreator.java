package org.springframework.cloud;

import org.springframework.cloud.CloudTestUtil.StubServiceInfo;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class StubServiceConnectorCreator extends AbstractServiceConnectorCreator<StubServiceConnector, StubServiceInfo> {
	@Override
	public StubServiceConnector create(StubServiceInfo serviceInfo, ServiceConnectorConfig config) {
		return new StubServiceConnector(serviceInfo.getId(), serviceInfo.getHost(), serviceInfo.getPort(), serviceInfo.getUserName(), serviceInfo.getPassword(),
				config == null ? null : ((StubServiceConnectorConfig)config).config);
	}

}