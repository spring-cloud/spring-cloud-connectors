package org.springframework.cloud.service.relational;

import java.sql.DriverManager;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import static org.springframework.cloud.service.Util.*;

/**
 * 
 * @author Ramnivas Laddad
 *
 * @param <SI>
 */
public abstract class DataSourceCreator<SI extends RelationalServiceInfo> extends AbstractServiceConnectorCreator<DataSource, SI> {

	private DataSourceConfigurer configurer = new DataSourceConfigurer();
	
	public abstract String getDriverClassName();

	public abstract String getValidationQuery();

	@Override
	public DataSource create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		try {
			Class.forName(getDriverClassName());
			// Give first preference to user's DBCP datasource
			if (hasClass("org.apache.commons.dbcp.BasicDataSource")) {
				org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
				setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig);
				return ds;
				// else, we have one from Tomcat
			} else if (hasClass("org.apache.tomcat.dbcp.dbcp.BasicDataSource")) {
				org.apache.tomcat.dbcp.dbcp.BasicDataSource ds = new org.apache.tomcat.dbcp.dbcp.BasicDataSource();
				setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig);
				return ds;
			} else {
				// Only for testing outside Tomcat/CloudFoundry
				return new SimpleDriverDataSource(DriverManager.getDriver(serviceInfo.getUrl()),
						serviceInfo.getUrl(), 
						serviceInfo.getUserName(), 
						serviceInfo.getPassword());
			}
		} catch (Exception e) {
			throw new ServiceConnectorCreationException(
					"Failed to created cloud datasource for "
							+ serviceInfo.getId() + " service", e);
		}
	}

	private void setBasicDataSourceProperties(DataSource basicDataSource, RelationalServiceInfo serviceInfo,
											  ServiceConnectorConfig serviceConnectorConfig) {
		BeanWrapper target = new BeanWrapperImpl(basicDataSource);
		target.setPropertyValue("driverClassName", getDriverClassName());
		target.setPropertyValue("url", serviceInfo.getUrl());
		target.setPropertyValue("username", serviceInfo.getUserName());
		target.setPropertyValue("password", serviceInfo.getPassword());
		if (getValidationQuery() != null) {
			target.setPropertyValue("validationQuery", getValidationQuery());
			target.setPropertyValue("testOnBorrow", true);
		}
		configurer.configure(basicDataSource, (DataSourceConfig)serviceConnectorConfig);
	}
}
