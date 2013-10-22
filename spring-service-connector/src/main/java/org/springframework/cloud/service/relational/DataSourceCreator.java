package org.springframework.cloud.service.relational;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * 
 * @author Ramnivas Laddad
 *
 * @param <SI>
 */
public abstract class DataSourceCreator<SI extends RelationalServiceInfo> extends AbstractServiceConnectorCreator<DataSource, SI> {

	protected static Logger logger = Logger.getLogger(DataSourceCreator.class.getName());
	
	public abstract String getDriverClassName();

	public abstract String getValidationQuery();

	private List<PooledDataSourceCreator<SI>> pooledDataSourceCreators = new ArrayList<PooledDataSourceCreator<SI>>();
	
	public DataSourceCreator() {
		if (pooledDataSourceCreators.size() == 0) {
			pooledDataSourceCreators.add(new BasicDbcpPooledDataSourceCreator<SI>());
			pooledDataSourceCreators.add(new TomcatDbcpPooledDataSourceCreator<SI>());
			pooledDataSourceCreators.add(new TomcatHighPerformancePooledDataSourceCreator<SI>());
		}
	}
	
	@Override
	public DataSource create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		try {
			Class.forName(getDriverClassName());
			
			for (PooledDataSourceCreator<SI> delegate: pooledDataSourceCreators) {
				DataSource ds = delegate.create(serviceInfo, serviceConnectorConfig, getDriverClassName(), getValidationQuery());
				
				if (ds != null) {
					return ds;
				}
			}
			// Only for testing outside Tomcat/CloudFoundry
			logger.warning("Found neither DBCP nor Tomcat connection pool on the classpath (no pooling is in effect).");
			return new SimpleDriverDataSource(DriverManager.getDriver(serviceInfo.getJdbcUrl()), serviceInfo.getJdbcUrl());
		} catch (Exception e) {
			throw new ServiceConnectorCreationException(
					"Failed to created cloud datasource for "
							+ serviceInfo.getId() + " service", e);
		}
	}
}
