package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.mockito.Mock;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractDataSourceFactoryTest<SI extends RelationalServiceInfo> extends AbstractCloudServiceConnectorFactoryTest<CloudDataSourceFactory, DataSource, SI> {
	@Mock DataSource mockDataSource;
	
	public CloudDataSourceFactory createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config) {
		return new CloudDataSourceFactory(id, config);
	}
	
	public Class<DataSource> getConnectorType() {
		return DataSource.class;
	}
	
	public DataSource getMockConnector() {
		return mockDataSource;
	}
}
