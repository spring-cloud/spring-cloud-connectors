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
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
																				dataSourceRequiringDecodedUrl());

		Connection connection = urlDecodingDataSource.getConnection();

		assertNotNull("Returned connection must not be null", connection);
		assertEquals(DECODED_URL, delegateDataSource.url);
		assertEquals("Connection must be made using the pooled (delegate) data source", TRUE, connection.getClientInfo(IS_POOLED));
	}

	@Test(expected = SQLException.class)
	public void whenNoConnectionIsSuccessful() throws SQLException {
		MockDataSource delegateDataSource = pooledDataSourceUnableToConnectToAnyUrl();
		UrlDecodingDataSource urlDecodingDataSource = new UrlDecodingDataSource(delegateDataSource, URL_PROPERTY_NAME,
																				dataSourceUnableToConnectToAnyUrl());

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
																				factoryFor(decodedUrlConnectionTestDataSource));

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
																				factoryFor(decodedUrlConnectionTestDataSource));

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

	private interface ConnectionSupplier {
		Connection getConnection(String url) throws SQLException;
	}

	private static MockDataSource pooledDataSourceUnableToConnectToAnyUrl() {
		return new MockDataSource(ENCODED_URL, withPooling(neverConnect()));
	}

	private static MockDataSource pooledDataSourceRequiringDecodedUrl() {
		return new MockDataSource(ENCODED_URL, withPooling(onlyConnectToDecodedUrls()));
	}

	private static MockDataSource pooledDataSourceRequiringEncodedUrl() {
		return new MockDataSource(ENCODED_URL, withPooling(onlyConnectToEncodedUrls()));
	}

	private static UrlDecodingDataSource.DataSourceFactory factoryFor(final DataSource dataSource) {
		return new UrlDecodingDataSource.DataSourceFactory() {
			@Override
			public DataSource apply(String jdbcUrl) {
				return dataSource;
			}
		};
	}

	private static UrlDecodingDataSource.DataSourceFactory dataSourceUnableToConnectToAnyUrl() {
		return new UrlDecodingDataSource.DataSourceFactory() {
			@Override
			public DataSource apply(String jdbcUrl) {
				return new MockDataSource(jdbcUrl, neverConnect());
			}
		};
	}

	private static UrlDecodingDataSource.DataSourceFactory dataSourceRequiringDecodedUrl() {
		return new UrlDecodingDataSource.DataSourceFactory() {
			@Override
			public DataSource apply(String jdbcUrl) {
				return new MockDataSource(jdbcUrl, onlyConnectToDecodedUrls());
			}
		};
	}

	private static ConnectionSupplier neverConnect() {
		return new ConnectionSupplier() {
			@Override
			public Connection getConnection(String url) throws SQLException {
				throw new SQLException("unable to connect to " + url);
			}
		};
	}

	private interface ConnectionTest {
		boolean apply(String url, String decodedUrl);
	}

	private static ConnectionSupplier withPooling(final ConnectionSupplier supplier) {
		return new ConnectionSupplier() {
			@Override
			public Connection getConnection(String url) throws SQLException {
				Connection connection = supplier.getConnection(url);
				when(connection.getClientInfo(IS_POOLED)).thenReturn(TRUE);
				return connection;
			}
		};
	}

	private static ConnectionSupplier onlyConnectToDecodedUrls() {
		return new ConnectionSupplier() {
			@Override
			public Connection getConnection(String url) throws SQLException {
				return onlyConnectToUrlsPassingDecodingTest(url, new ConnectionTest() {
					@Override
					public boolean apply(String url, String decodedUrl) {
						if (url == null || decodedUrl == null) return false;
						return url.equals(decodedUrl);
					}
				});
			}
		};
	}

	private static ConnectionSupplier onlyConnectToEncodedUrls() {
		return new ConnectionSupplier() {
			@Override
			public Connection getConnection(String url) throws SQLException {
				return onlyConnectToUrlsPassingDecodingTest(url, new ConnectionTest() {
					@Override
					public boolean apply(String url, String decodedUrl) {
						if (url == null || decodedUrl == null) return false;
						return !url.equals(decodedUrl);
					}
				});
			}
		};
	}

	private static Connection onlyConnectToUrlsPassingDecodingTest(String url, ConnectionTest urlTest) throws SQLException {
		try {
			String decodedUrl = URLDecoder.decode(url, "UTF-8");
			if (urlTest.apply(url, decodedUrl)) {
				return mock(Connection.class);
			} else {
				throw new SQLException("unable to connect to " + url);
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static UrlDecodingDataSource.DataSourceFactory dataSourceFactory(final DataSource dataSource) {
		return new UrlDecodingDataSource.DataSourceFactory() {
			@Override
			public DataSource apply(String jdbcUrl) {
				return dataSource;
			}
		};
	}
}
