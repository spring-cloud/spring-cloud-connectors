package org.springframework.cloud.service.relational;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
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

    private List<PooledDataSourceCreator<SI>> pooledDataSourceCreators = new ArrayList<PooledDataSourceCreator<SI>>();

	public DataSourceCreator(String driverSystemPropKey, String[] driverClasses, String validationQuery) {
	    this.driverSystemPropKey = driverSystemPropKey;
	    this.driverClasses = driverClasses;
	    this.validationQuery = validationQuery;

		if (pooledDataSourceCreators.size() == 0) {
			pooledDataSourceCreators.add(new BasicDbcpPooledDataSourceCreator<SI>());
			pooledDataSourceCreators.add(new TomcatDbcpPooledDataSourceCreator<SI>());
			pooledDataSourceCreators.add(new TomcatHighPerformancePooledDataSourceCreator<SI>());
			pooledDataSourceCreators.add(new HikariCpPooledDataSourceCreator<SI>());
		}
	}

	@Override
	public DataSource create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		try {
			for (PooledDataSourceCreator<SI> delegate: pooledDataSourceCreators) {
				DataSource ds = delegate.create(serviceInfo, serviceConnectorConfig, getDriverClassName(serviceInfo), validationQuery);

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
