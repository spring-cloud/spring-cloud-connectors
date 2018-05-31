/*
 *  Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.HANAServiceInfo;
import org.springframework.cloud.util.UriInfo;

import java.util.Map;

/**
 * @author Johannes Roesch
 */
public class HANAServiceInfoCreator
		extends RelationalServiceInfoCreator<HANAServiceInfo> {
	public HANAServiceInfoCreator() {
		super(new Tags("hana", "database", "relational"), HANAServiceInfo.HANA_SCHEME);
	}

	@Override public HANAServiceInfo createServiceInfo(String id, String url,
			String jdbcUrl) {
		return new HANAServiceInfo(id, url, jdbcUrl);
	}

	@Override public HANAServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = getId(serviceData);

		Map<String, Object> credentials = getCredentials(serviceData);

		String host = getStringFromCredentials(credentials, "hostname", "host");
		int port = getIntFromCredentials(credentials, "port");

		String username = UriInfo
				.urlEncode(getStringFromCredentials(credentials, "user", "username"));
		String password = UriInfo.urlEncode((String) credentials.get("password"));

		String database = (String) credentials.get("schema");

		String uri = String
				.format("jdbc:sap://%s:%s/?currentschema=%s&user=%s&password=%s", host,
						port, database, username, password);
		String url = String
				.format("sap://%s:%s@%s:%s/%s", username, password, host, port, database);

		return createServiceInfo(id, url, uri);
	}
}
