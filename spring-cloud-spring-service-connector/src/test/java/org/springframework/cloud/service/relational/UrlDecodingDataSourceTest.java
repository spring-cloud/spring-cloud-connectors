/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.service.relational;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.jdbc.datasource.AbstractDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlDecodingDataSourceTest {

	private static final String ENCODED_URL = "jdbc:mysql://host.example.com:3306/db?user=user%40host&password=password%21";
	private static final String DECODED_URL = "jdbc:mysql://host.example.com:3306/db?user=user@host&password=password!";
	private static final String URL_PROPERTY_NAME = "url";
	private static final String IS_POOLED = "is pooled";
	private static final String TRUE = "true";

	@Test
	public void whenConnectionToEncodedUrlIsSuccessful() throws SQLException {
		MockDataSource delegateDataSource = pooledDataSourceRequiringEncodedUrl();
		UrlDecodingDataSource urlDecodingDataSource = new UrlDecodingDataSource(delegateDataSource, "url");

		Connection connection = urlDecodingDataSource.getConnection();

		assertNotNull("Returned connection must not be null", connection);
		assertEquals(ENCODED_URL, delegateDataSource.url);
		assertEquals("Connection must be made using the pooled (delegate) data source", TRUE, connection.getClientInfo(IS_POOLED));
	}

	@Test
	public void whenConnectionToDecodedUrlIsSuccessful() throws SQLException {
		MockDataSource delegateDataSource = pooledDataSourceRequiringDecodedUrl();
		UrlDecodingDataSource urlDecodingDataSource = new UrlDecodingDataSource(delegateDataSource, URL_PROPERTY_NAME,
																				UrlDecodingDataSourceTest::dataSourceRequiringDecodedUrl);

		Connection connection = urlDecodingDataSource.getConnection();

		assertNotNull("Returned connection must not be null", connection);
		assertEquals(DECODED_URL, delegateDataSource.url);
		assertEquals("Connection must be made using the pooled (delegate) data source", TRUE, connection.getClientInfo(IS_POOLED));
	}

	@Test(expected = SQLException.class)
	public void whenNoConnectionIsSuccessful() throws SQLException {
		MockDataSource delegateDataSource = pooledDataSourceUnableToConnectToAnyUrl();
		UrlDecodingDataSource urlDecodingDataSource = new UrlDecodingDataSource(delegateDataSource, URL_PROPERTY_NAME,
																				UrlDecodingDataSourceTest::dataSourceUnableToConnectToAnyUrl);

		try {
			urlDecodingDataSource.getConnection();
		} catch (SQLException e) {
			assertEquals(ENCODED_URL, delegateDataSource.url);
			throw e;
		}
	}

	@Test
	public void successfulUrlDecodedConnectionTestIsOnlyPerformedOnce() throws SQLException {
		MockDataSource delegateDataSource = pooledDataSourceRequiringDecodedUrl();
		DataSource decodedUrlConnectionTestDataSource = mock(DataSource.class);
		when(decodedUrlConnectionTestDataSource.getConnection()).thenReturn(mock(Connection.class));
		UrlDecodingDataSource urlDecodingDataSource = new UrlDecodingDataSource(delegateDataSource, URL_PROPERTY_NAME,
																				url -> decodedUrlConnectionTestDataSource);

		urlDecodingDataSource.getConnection();
		urlDecodingDataSource.getConnection();

		verify(decodedUrlConnectionTestDataSource, times(1)).getConnection();
	}

	@Test
	public void unsuccessfulUrlDecodedConnectionTestIsTriedAgain() throws SQLException {
		MockDataSource delegateDataSource = pooledDataSourceRequiringDecodedUrl();
		DataSource decodedUrlConnectionTestDataSource = mock(DataSource.class);
		when(decodedUrlConnectionTestDataSource.getConnection()).thenThrow(new SQLException("unable to connect"));
		UrlDecodingDataSource urlDecodingDataSource = new UrlDecodingDataSource(delegateDataSource, URL_PROPERTY_NAME,
																				url -> decodedUrlConnectionTestDataSource);

		try {
			urlDecodingDataSource.getConnection();
		} catch (SQLException e) {}
		try {
			urlDecodingDataSource.getConnection();
		} catch (SQLException e) {}

		verify(decodedUrlConnectionTestDataSource, times(2)).getConnection();
	}

	private static class MockDataSource extends AbstractDataSource {
		private final ConnectionSupplier connectionSupplier;
		private String url;

		MockDataSource(String url, ConnectionSupplier connectionSupplier) {
			this.url = url;
			this.connectionSupplier = connectionSupplier;
		}

		@Override
		public Connection getConnection() throws SQLException {
			return connectionSupplier.getConnection(url);
		}

		@Override
		public Connection getConnection(String username, String password) {
			throw new UnsupportedOperationException();
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	@FunctionalInterface
	private interface ConnectionSupplier {
		Connection getConnection(String url) throws SQLException;
	}

	private static Connection withPooling(ConnectionSupplier supplier, String url) throws SQLException {
		Connection connection = supplier.getConnection(url);
		when(connection.getClientInfo(IS_POOLED)).thenReturn(TRUE);
		return connection;
	}

	private static MockDataSource pooledDataSourceUnableToConnectToAnyUrl() {
		return new MockDataSource(ENCODED_URL, u -> withPooling(UrlDecodingDataSourceTest::neverConnect, u));
	}

	private static MockDataSource pooledDataSourceRequiringDecodedUrl() {
		return new MockDataSource(ENCODED_URL, u -> withPooling(UrlDecodingDataSourceTest::onlyConnectToDecodedUrls, u));
	}

	private static MockDataSource pooledDataSourceRequiringEncodedUrl() {
		return new MockDataSource(ENCODED_URL, u -> withPooling(UrlDecodingDataSourceTest::onlyConnectToEncodedUrls, u));
	}

	private static MockDataSource dataSourceUnableToConnectToAnyUrl(String url) {
		return new MockDataSource(url, UrlDecodingDataSourceTest::neverConnect);
	}

	private static MockDataSource dataSourceRequiringDecodedUrl(String url) {
		return new MockDataSource(url, UrlDecodingDataSourceTest::onlyConnectToDecodedUrls);
	}

	private static Connection neverConnect(String url) throws SQLException {
		throw new SQLException("unable to connect to " + url);
	}

	private static Connection onlyConnectToDecodedUrls(String url) throws SQLException {
		return onlyConnectToUrlsPassingDecodingTest(url, Objects::equals);
	}

	private static Connection onlyConnectToEncodedUrls(String url) throws SQLException {
		return onlyConnectToUrlsPassingDecodingTest(url, (original, decodedUrl) -> !Objects.equals(original, decodedUrl));
	}

	private static Connection onlyConnectToUrlsPassingDecodingTest(String url, BiFunction<String, String, Boolean> urlTest) throws SQLException {
		try {
			String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.name());
			if (urlTest.apply(url, decodedUrl)) {
				return mock(Connection.class);
			} else {
				throw new SQLException("unable to connect to " + url);
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
