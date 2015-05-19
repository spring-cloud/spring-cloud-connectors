package org.springframework.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * @author Ramnivas Laddad
 * @author Scott Frederick
 */
public abstract class RelationalServiceInfo extends UriBasedServiceInfo {

	public static final String JDBC_PREFIX = "jdbc:";

	protected final String jdbcUrlDatabaseType;

	public RelationalServiceInfo(String id, String uriString, String jdbcUrlDatabaseType) {
		super(id, uriString);
		this.jdbcUrlDatabaseType = jdbcUrlDatabaseType;
	}

	@ServiceProperty(category = "connection")
	public String getJdbcUrl() {
		if (getUriInfo().getUriString().startsWith(JDBC_PREFIX)) {
			return getUriInfo().getUriString();
		}

		return String.format("%s%s://%s%s/%s%s%s", JDBC_PREFIX, jdbcUrlDatabaseType, getHost(), formatPort(),
				getPath(), formatUserinfo(), formatQuery());
	}

	private String formatPort() {
		if (getPort() != -1) {
			return String.format(":%d", getPort());
		}
		return "";
	}

	private String formatUserinfo() {
		if (getUserName() != null && getPassword() != null) {
			return String.format("?user=%s&password=%s", getUserName(), getPassword());
		}
		if (getUserName() != null) {
			return String.format("?user=%s", getUserName());
		}
		return "";
	}

	private String formatQuery() {
		if (getQuery() != null) {
			if (getUserName() == null && getPassword() == null) {
				return String.format("?%s", getQuery());
			} else {
				return String.format("&%s", getQuery());
			}
		}
		return "";
	}
}
