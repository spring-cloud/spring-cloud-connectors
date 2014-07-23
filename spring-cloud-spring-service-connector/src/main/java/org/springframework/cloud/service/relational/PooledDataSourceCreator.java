package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * DataSource creator that produces a pooled connection
 *
 * @author Ramnivas Laddad
 *
 * @param <SI> the {@link RelationalServiceInfo} type for the underlying database service
 */
public interface PooledDataSourceCreator<SI extends RelationalServiceInfo> {
	public abstract DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
			                          String driverClassName, String validationQuery);
}

