package org.springframework.cloud.service.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RelationalServiceInfoTest {

	@Test
	public void jdbcUrl() {
		RelationalServiceInfo serviceInfo = createServiceInfoWithJdbcUrl("bad://bad:bad@bad:1234/bad",
						"jdbc:jdbcdbtype://hostname:1234/database?user=username&password=password");

		assertEquals("jdbc:jdbcdbtype://hostname:1234/database?user=username&password=password", serviceInfo.getJdbcUrl());
	}

	@Test
	public void jdbcFullUrl() {
		RelationalServiceInfo serviceInfo = createServiceInfo("dbtype://username:password@hostname:1234/database");

		assertEquals("jdbc:jdbcdbtype://hostname:1234/database?user=username&password=password", serviceInfo.getJdbcUrl());
	}

	@Test
	public void jdbcUrlNoPort() {
		RelationalServiceInfo serviceInfo = createServiceInfo("dbtype://username:password@hostname/database");

		assertEquals("jdbc:jdbcdbtype://hostname/database?user=username&password=password", serviceInfo.getJdbcUrl());
	}

	@Test
	public void jdbcUrlNoUsernamePassword() {
		RelationalServiceInfo serviceInfo = createServiceInfo("dbtype://hostname:1234/database");

		assertEquals("jdbc:jdbcdbtype://hostname:1234/database", serviceInfo.getJdbcUrl());
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void jdbcUrlNoPassword() {
		createServiceInfo("dbtype://username@hostname/database");
	}

	@Test
	public void jdbcUrlWithQuery() {
		RelationalServiceInfo serviceInfo = createServiceInfo("dbtype://username:password@hostname:1234/database?reconnect=true");

		assertEquals("jdbc:jdbcdbtype://hostname:1234/database?user=username&password=password&reconnect=true", serviceInfo.getJdbcUrl());
	}

	@Test
	public void jdbcUrlWithQueryNoUsernamePassword() {
		RelationalServiceInfo serviceInfo = createServiceInfo("dbtype://hostname:1234/database?reconnect=true");

		assertEquals("jdbc:jdbcdbtype://hostname:1234/database?reconnect=true", serviceInfo.getJdbcUrl());
	}

	private RelationalServiceInfo createServiceInfo(final String uri) {
		return new RelationalServiceInfo("test", uri, null, "jdbcdbtype") {
		};
	}

	private RelationalServiceInfo createServiceInfoWithJdbcUrl(final String uri, final String jdbcUrl) {
		return new RelationalServiceInfo("test", uri, jdbcUrl, "jdbcdbtype") {
		};
	}

}