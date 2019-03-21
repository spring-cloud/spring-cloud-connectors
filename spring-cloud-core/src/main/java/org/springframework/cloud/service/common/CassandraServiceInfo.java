/*
 * Copyright 2016 the original author or authors.
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

package org.springframework.cloud.service.common;

import java.util.List;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * {@link org.springframework.cloud.service.ServiceInfo} for a Cassandra/Datastax DSE
 * service.
 *
 * @author Mark Paluch
 */
@ServiceLabel("cassandra")
public class CassandraServiceInfo extends BaseServiceInfo {

	private final List<String> contactPoints;
	private final int port;
	private final String username;
	private final String password;

	/**
	 * Creates a new {@link CassandraServiceInfo} using Contact points and port.
	 *
	 * @param id the service-id
	 * @param contactPoints list of contact-points
	 * @param port the port
	 */
	public CassandraServiceInfo(String id, List<String> contactPoints, int port) {
		this(id, contactPoints, port, null, null);
	}

	/**
	 * Creates a new {@link CassandraServiceInfo} using Contact points with a port and
	 * username/password credentials.
	 *
	 * @param id the service-id
	 * @param contactPoints list of contact-points
	 * @param port the port
	 * @param username the user name
	 * @param password the password
	 */
	public CassandraServiceInfo(String id, List<String> contactPoints, int port,
			String username, String password) {
		super(id);

		this.contactPoints = contactPoints;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	@ServiceProperty(category = "contactpoints")
	public List<String> getContactPoints() {
		return contactPoints;
	}

	@ServiceProperty(category = "port")
	public int getPort() {
		return port;
	}

	@ServiceProperty(category = "username")
	public String getUsername() {
		return username;
	}

	@ServiceProperty(category = "password")
	public String getPassword() {
		return password;
	}
}
