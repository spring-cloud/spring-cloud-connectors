package org.springframework.cloud.service.relational;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 *
 * @author Ramnivas Laddad
 *
 * @param <SI> the {@link RelationalServiceInfo} for the backing database service
 */
public abstract class DataSourceCreator<SI extends RelationalServiceInfo> extends AbstractServiceConnectorCreator<DataSource, SI> {

	protected static Logger logger = Logger.getLogger(DataSourceCreator.class.getName());

	private String driverSystemPropKey;
	private String[] driverClasses;
	private String validationQuery;

	private Map<String, PooledDataSourceCreator<SI>> pooledDataSourceCreators =
			new LinkedHashMap<String, PooledDataSourceCreator<SI>>();

	public DataSourceCreator(String driverSystemPropKey, String[] driverClasses, String validationQuery) {
		this.driverSystemPropKey = driverSystemPropKey;
		this.driverClasses = driverClasses;
		this.validationQuery = validationQuery;

		if (pooledDataSourceCreators.size() == 0) {
			putPooledDataSourceCreator(new BasicDbcpPooledDataSourceCreator<SI>());
			putPooledDataSourceCreator(new TomcatDbcpPooledDataSourceCreator<SI>());
			putPooledDataSourceCreator(new TomcatJdbcPooledDataSourceCreator<SI>());
			putPooledDataSourceCreator(new HikariCpPooledDataSourceCreator<SI>());
		}
	}

	private void putPooledDataSourceCreator(PooledDataSourceCreator<SI> pooledDataSourceCreator) {
		pooledDataSourceCreators.put(pooledDataSourceCreator.getClass().getSimpleName(), pooledDataSourceCreator);
	}

	@Override
	public DataSource create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		try {
			DataSource ds = createPooledDataSource(serviceInfo, serviceConnectorConfig);
			if (ds != null) {
				return ds;
			}
			// Only for testing outside Tomcat/CloudFoundry
			logger.warning("No connection pooling DataSource implementation found on the classpath - no pooling is in effect.");
			return new SimpleDriverDataSource(DriverManager.getDriver(serviceInfo.getJdbcUrl()), serviceInfo.getJdbcUrl());
		} catch (Exception e) {
			throw new ServiceConnectorCreationException(
					"Failed to created cloud datasource for " + serviceInfo.getId() + " service", e);
		}
	}

	private DataSource createPooledDataSource(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		Collection<PooledDataSourceCreator<SI>> delegates = filterPooledDataSourceCreators(serviceConnectorConfig);

		for (PooledDataSourceCreator<SI> delegate : delegates) {
			DataSource ds = delegate.create(serviceInfo, serviceConnectorConfig, getDriverClassName(serviceInfo), validationQuery);
			if (ds != null) {
				return ds;
			}
		}

		return null;
	}

	private Collection<PooledDataSourceCreator<SI>> filterPooledDataSourceCreators(ServiceConnectorConfig serviceConnectorConfig) {
		if (serviceConnectorConfig != null) {
			List<String> pooledDataSourceNames = ((DataSourceConfig) serviceConnectorConfig).getPooledDataSourceNames();
			if (pooledDataSourceNames != null) {
				List<PooledDataSourceCreator<SI>> filtered = new ArrayList<PooledDataSourceCreator<SI>>();

				for (String name : pooledDataSourceNames) {
					for (String key : pooledDataSourceCreators.keySet()) {
						if (key.contains(name)) {
							filtered.add(pooledDataSourceCreators.get(key));
						}
					}
				}

				return filtered;
			}
		}
		return pooledDataSourceCreators.values();
	}

	public String getDriverClassName(SI serviceInfo) {
		String userSpecifiedDriver = System.getProperty(driverSystemPropKey);

		if (userSpecifiedDriver != null && !userSpecifiedDriver.isEmpty()) {
			return userSpecifiedDriver;
		} else {
			for (String driver : driverClasses) {
				try {
					Class.forName(driver);
					return driver;
				} catch (ClassNotFoundException ex) {
					// continue...
				}
			}
		}
		throw new CloudException("No suitable database driver found for " + serviceInfo.getId() + " service ");
	}
}
