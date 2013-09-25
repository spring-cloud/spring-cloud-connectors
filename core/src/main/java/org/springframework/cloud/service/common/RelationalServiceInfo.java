package org.springframework.cloud.service.common;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfo extends BaseServiceInfo {
	protected String jdbcUrl;
	
	public RelationalServiceInfo(String id, String uriString, String jdbcUrlDatabaseType) {
		super(id, uriString);
		this.jdbcUrl = 
			String.format("jdbc:%s://%s:%d/%s?user=%s&password=%s", 
					      jdbcUrlDatabaseType,
					      getHost(), getPort(), getPath(), getUserName(), getPassword());
	}
	
	@ServiceProperty(category="connection")
	public String getJdbcUrl() {
		return jdbcUrl;
	}
}
