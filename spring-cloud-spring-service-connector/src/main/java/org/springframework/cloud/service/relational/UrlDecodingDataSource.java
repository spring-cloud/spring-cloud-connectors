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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * <p>
 * {@link UrlDecodingDataSource} is used to transparently handle combinations of Clouds and database
 * drivers which may or may not provide or accept URL-encoded connection strings.
 * </p>
 *
 * <p>
 * This {@link DataSource} implementation transparently delegates to an underlying {@link DataSource},
 * except in the case where a connection attempt is made and no previous connection attempt has been
 * successful. In this case, if the underlying {@link DataSource} fails to connect,
 * {@link UrlDecodingDataSource} makes a test connection using a URL-decoded version of the configured
 * JDBC URL. If this is successful, the underlying {@link DataSource} is updated with the decoded JDBC
 * URL, and used to establish the final connection which is returned to the client.
 * </p>
 *
 * @author Gareth Clay
 */
class UrlDecodingDataSource extends DelegatingDataSource {

	private static final Logger logger = Logger.getLogger(DelegatingDataSource.class.getName());

	private final String urlPropertyName;
	private final Function<String, DataSource> connectionTestDataSourceFactory;

	private volatile boolean successfullyConnected = false;

	UrlDecodingDataSource(String jdbcUrl) {
		this(newSimpleDriverDataSource(jdbcUrl), "url");
	}

	UrlDecodingDataSource(DataSource targetDataSource, String urlPropertyName) {
		this(targetDataSource, urlPropertyName, UrlDecodingDataSource::newSimpleDriverDataSource);
	}

	UrlDecodingDataSource(DataSource targetDataSource, String urlPropertyName, Function<String, DataSource> connectionTestDataSourceFactory) {
		super(targetDataSource);
		this.urlPropertyName = urlPropertyName;
		this.connectionTestDataSourceFactory = connectionTestDataSourceFactory;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (successfullyConnected) return super.getConnection();

		synchronized (this) {
			Connection connection;

			try {
				connection = super.getConnection();
				successfullyConnected = true;
			} catch (SQLException e) {
				logger.info("Database connection failed. Trying again with url-decoded jdbc url");
				DataSource targetDataSource = getTargetDataSource();
				if (targetDataSource == null) throw new IllegalStateException("target DataSource should never be null");
				BeanWrapper dataSourceWrapper = new BeanWrapperImpl(targetDataSource);
				String decodedJdbcUrl = decode((String) dataSourceWrapper.getPropertyValue(urlPropertyName));
				DataSource urlDecodedConnectionTestDataSource = connectionTestDataSourceFactory.apply(decodedJdbcUrl);
				urlDecodedConnectionTestDataSource.getConnection();

				logger.info("Connection test successful. Continuing with url-decoded jdbc url");
				dataSourceWrapper.setPropertyValue(urlPropertyName, decodedJdbcUrl);
				connection = super.getConnection();
				successfullyConnected = true;
			}

			return connection;
		}
	}

	private static DataSource newSimpleDriverDataSource(String jdbcUrl) {
		try {
			return new SimpleDriverDataSource(DriverManager.getDriver(jdbcUrl), jdbcUrl);
		} catch (SQLException e) {
			throw new RuntimeException("Unable to construct DataSource", e);
		}
	}

	private static String decode(String string) {
		try {
			return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}
}
