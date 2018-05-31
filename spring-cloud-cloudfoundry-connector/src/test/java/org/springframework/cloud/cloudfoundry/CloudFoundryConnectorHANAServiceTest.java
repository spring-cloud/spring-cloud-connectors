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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.HANAServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.util.UriInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.service.common.HANAServiceInfo.HANA_SCHEME;

/**
 * @author Johannes Roesch
 */

public class CloudFoundryConnectorHANAServiceTest
		extends AbstractUserProvidedServiceInfoCreatorTest {
	private static final String INSTANCE_NAME = "database";
	private static final String SERVICE_NAME = "hana-ups";

	@Test public void hanaServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
				getVcapServices(SERVICE_NAME, hostname, port, username, password,
						INSTANCE_NAME));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, HANAServiceInfo.class);
		assertEquals(getJdbcUrl(hostname, port, INSTANCE_NAME, username, password),
				((RelationalServiceInfo) info).getJdbcUrl());
		assertUriBasedServiceInfoFields(info, HANA_SCHEME, hostname, port, username,
				password, INSTANCE_NAME);
	}

	@Test public void hanaServiceCreationWithSpecialChars() {
		String userWithSpecialChars = "u%u:u+";
		String passwordWithSpecialChars = "p%p:p+";
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
				getVcapServices(SERVICE_NAME, hostname, port, userWithSpecialChars,
						passwordWithSpecialChars, INSTANCE_NAME));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, HANAServiceInfo.class);
		assertEquals(getJdbcUrl(hostname, port, INSTANCE_NAME,
				UriInfo.urlEncode(userWithSpecialChars),
				UriInfo.urlEncode(passwordWithSpecialChars)),
				((RelationalServiceInfo) info).getJdbcUrl());
		assertUriBasedServiceInfoFields(info, HANA_SCHEME, hostname, port,
				userWithSpecialChars, passwordWithSpecialChars, INSTANCE_NAME);
	}

	@Test public void hanaServiceCreationWithNoUri() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
				getVcapServicesWOUri(SERVICE_NAME, hostname, port, username, password,
						INSTANCE_NAME));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertNotNull(info);
		assertFalse(MysqlServiceInfo.class
				.isAssignableFrom(info.getClass()));  // service was not detected as MySQL
		assertNotNull(info);
	}

	@Test public void hanaServiceCreationWithJdbcUrl() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES")).thenReturn(
				getVcapServices(SERVICE_NAME, hostname, port, username, password,
						INSTANCE_NAME));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info = getServiceInfo(serviceInfos, SERVICE_NAME);
		assertServiceFoundOfType(info, HANAServiceInfo.class);
		assertEquals(
				getJdbcUrl(hostname, port, INSTANCE_NAME, UriInfo.urlEncode(username),
						UriInfo.urlEncode(password)),
				((RelationalServiceInfo) info).getJdbcUrl());
		assertUriBasedServiceInfoFields(info, HANA_SCHEME, hostname, port, username,
				password, INSTANCE_NAME);
	}

	private String getJdbcUrl(String hostname, int port, String dbschema, String user,
			String password) {
		return String.format("jdbc:sap://%s:%s/?currentschema=%s&user=%s&password=%s",
				hostname, port, dbschema, user, password);

	}

	private String getVcapServices(final String name, final String host, final int port,
			final String user, final String password, final String database) {
		ServiceDataBuilder builder = new ServiceDataBuilder()
				.withTags("hana", "database", "relational").withLabel("hana")
				.withName(name).withCredentials("driver", "com.sap.db.jdbc.Driver")
				.withCredentials("hdi_password", "ABC123")
				.withCredentials("hdi_user", "123ABC").withCredentials("host", host)
				.withCredentials("user", user).withCredentials("password", password)
				.withCredentials("port", String.valueOf(port))
				.withCredentials("schema", database)
				.withCredentials("url", getJdbcUrl(host, port, database, user, password));

		try {
			return builder.buildVCAP();
		}
		catch (JsonProcessingException e) {
			return "{}";
		}
	}

	private String getVcapServicesWOUri(final String name, final String host,
			final int port, final String user, final String password,
			final String database) {
		ServiceDataBuilder builder = new ServiceDataBuilder()
				.withTags("hana", "database", "relational").withLabel("hana")
				.withName(name).withCredentials("driver", "com.sap.db.jdbc.Driver")
				.withCredentials("hdi_password", "ABC123")
				.withCredentials("hdi_user", "123ABC").withCredentials("host", host)
				.withCredentials("user", user).withCredentials("password", password)
				.withCredentials("port", String.valueOf(port))
				.withCredentials("schema", database);

		try {
			return builder.buildVCAP();
		}
		catch (JsonProcessingException e) {
			return "{}";
		}
	}

	private class ServiceDataBuilder {
		private String[] tags = new String[0];
		private String label = "";
		private String name = "";
		private Map<String, Object> credentials = new HashMap<String, Object>();

		public ServiceDataBuilder withTags(String... tags) {
			this.tags = tags;
			return this;
		}

		public ServiceDataBuilder withCredentials(String key, String value) {
			credentials.put(key, value);
			return this;
		}

		public ServiceDataBuilder withLabel(String label) {
			this.label = label;
			return this;
		}

		public ServiceDataBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public Map<String, Object> build() {
			Map<String, Object> serviceData = new HashMap<String, Object>();

			serviceData.put("name", name);
			serviceData.put("tags", Arrays.asList(tags));
			serviceData.put("label", label);
			serviceData.put("credentials", credentials);

			return serviceData;
		}

		public String buildVCAP() throws JsonProcessingException {
			Map<String, Object[]> map = new HashMap<>();
			Object[] ar = { build() };
			map.put(label, ar);

			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(map);
		}
	}

}
