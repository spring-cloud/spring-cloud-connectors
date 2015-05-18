package org.springframework.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.util.StandardUriInfoFactory;
import org.springframework.cloud.util.UriInfo;
import org.springframework.cloud.util.UriInfoFactory;

/**
 * @author Ramnivas Laddad
 * @author Scott Frederick
 */
public abstract class RelationalServiceInfo extends UriBasedServiceInfo {

	public static final String JDBC_PREFIX = "jdbc:";
	private static JdbcUriInfoFactory jdbcUriInfoFactory = new JdbcUriInfoFactory();

	protected final String jdbcUrlDatabaseType;

	public RelationalServiceInfo(String id, String uriString, String jdbcUrlDatabaseType) {
		super(id, uriString);
		this.jdbcUrlDatabaseType = jdbcUrlDatabaseType;
	}

	@Override
	public UriInfoFactory getUriInfoFactory() {
		return jdbcUriInfoFactory;
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

	public static class JdbcUriInfoFactory extends StandardUriInfoFactory {
		@Override
		public UriInfo createUri(String uriString) {
			if (uriString.startsWith(JDBC_PREFIX)) {
				return new JdbcUriInfo(uriString);
			}
			return super.createUri(uriString);
		}
	}

	public static class JdbcUriInfo extends UriInfo {
		public JdbcUriInfo(String rawUriString) {
			super(rawUriString);
		}
	}
}
