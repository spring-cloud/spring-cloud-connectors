/*
 * Copyright 2017 the original author or authors.
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

package org.springframework.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.service.common.CassandraServiceInfo;


/**
 * Service info creator for Cassandra services.
 *
 * @author Mark Paluch
 */
public class CassandraServiceInfoCreator extends
		CloudFoundryServiceInfoCreator<CassandraServiceInfo> {

	public CassandraServiceInfoCreator() {
		super(new Tags("cassandra"));
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return cassandraCredentialsPresent(serviceData);
	}

	private boolean cassandraCredentialsPresent(Map<String, Object> serviceData) {
		Map<String, Object> credentials = getCredentials(serviceData);
		return credentials != null &&
				credentials.containsKey("cqlsh_port") &&
				credentials.containsKey("node_ips");
	}

	@Override
	@SuppressWarnings("unchecked")
	public CassandraServiceInfo createServiceInfo(Map<String, Object> serviceData) {

		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);

		String username = getStringFromCredentials(credentials, "username");
		String password = getStringFromCredentials(credentials, "password");
		int port = getIntFromCredentials(credentials, "cqlsh_port");
		List<String> contactPoints = (List<String>) credentials.get("node_ips");

		return new CassandraServiceInfo(id, contactPoints, port,
				username, password);
	}
}
