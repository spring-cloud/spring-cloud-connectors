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

package org.springframework.cloud.cloudfoundry;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.service.common.CassandraServiceInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for link {@link CassandraServiceInfoCreator}.
 *
 * @author Mark Paluch
 */
public class CassandraServiceInfoCreatorTests extends AbstractCloudFoundryConnectorTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void shouldCreateServiceInfo() throws Exception {

		CassandraServiceInfoCreator creator = new CassandraServiceInfoCreator();
		Map services = readServiceData("test-cassandra-service.json");
		Map<String, Object> serviceData = getServiceData(services,
				"p-dse-cassandra-acceptance");

		CassandraServiceInfo info = creator.createServiceInfo(serviceData);

		assertThat(info.getContactPoints(), hasItems("1.2.3.4", "5.6.7.8"));
		assertThat(info.getPort(), is(equalTo(9042)));
		assertThat(info.getUsername(), is(nullValue()));
		assertThat(info.getPassword(), is(nullValue()));
	}

	@Test
	public void shouldCreateServiceInfoWithCredentials() throws Exception {

		CassandraServiceInfoCreator creator = new CassandraServiceInfoCreator();
		Map services = readServiceData("test-cassandra-with-credentials.json");
		Map<String, Object> serviceData = getServiceData(services,
				"p-dse-cassandra-acceptance");

		CassandraServiceInfo info = creator.createServiceInfo(serviceData);

		assertThat(info.getContactPoints(), hasItems("1.2.3.4"));
		assertThat(info.getPort(), is(equalTo(9042)));
		assertThat(info.getUsername(), is(equalTo("user")));
		assertThat(info.getPassword(), is(equalTo("pass")));
	}

	@Test
	public void shouldAcceptService() throws Exception {

		CassandraServiceInfoCreator creator = new CassandraServiceInfoCreator();
		Map services = readServiceData("test-cassandra-service.json");
		Map<String, Object> serviceData = getServiceData(services,
				"p-dse-cassandra-acceptance");

		assertThat(creator.accept(serviceData), is(true));
	}

	@Test
	public void shouldNotAcceptServiceWithRequiredFieldsMissing() throws Exception {

		CassandraServiceInfoCreator creator = new CassandraServiceInfoCreator();
		Map services = readServiceData("test-cassandra-missing-required-fields.json");
		Map<String, Object> serviceData = getServiceData(services,
				"p-dse-cassandra-acceptance");

		assertThat(creator.accept(serviceData), is(false));
	}

	private Map readServiceData(String resource) throws java.io.IOException {
		return mapper.readValue(readTestDataFile(resource), Map.class);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getServiceData(Map services, String name) {
		return (Map<String, Object>) ((List) services.get(name)).get(0);
	}
}
