package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Spring factory bean for datasource service.
 *
 * @author Ramnivas Laddad
 *
 */
public class CloudDataSourceFactory extends AbstractCloudServiceConnectorFactory<DataSource> {
	public CloudDataSourceFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, DataSource.class, serviceConnectorConfiguration);
	}
}
