package org.springframework.cloud.service.common;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfo extends BaseServiceInfo {
	protected String jdbcUrl;
	
	public RelationalServiceInfo(String id, String uriString) {
		super(id, uriString);
		this.jdbcUrl = "jdbc:" + uriString;
	}
	
	@ServiceProperty(category="connection")
	public String getJdbcUrl() {
		return jdbcUrl;
	}
}
