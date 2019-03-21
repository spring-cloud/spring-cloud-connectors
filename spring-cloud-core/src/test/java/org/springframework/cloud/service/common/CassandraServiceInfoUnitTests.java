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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collections;


import org.junit.Test;

/**
 * Unit tests for {@link CassandraServiceInfo}.
 *
 * @author Mark Paluch
 */
public class CassandraServiceInfoUnitTests {

	@Test
	public void shouldContainContactPointsAndPort() {

		CassandraServiceInfo info = new CassandraServiceInfo("cassandra",
				Collections.singletonList("10.0.0.1"), 9042);

		assertThat(info.getContactPoints(), hasItem("10.0.0.1"));
		assertThat(info.getPort(), is(equalTo(9042)));
	}

	@Test
	public void shouldContainContactPointsAndPortAndCredentials() {

		CassandraServiceInfo info = new CassandraServiceInfo("cassandra",
				Collections.singletonList("10.0.0.1"), 9042, "walter", "white");

		assertThat(info.getContactPoints(), hasItem("10.0.0.1"));
		assertThat(info.getPort(), is(equalTo(9042)));
		assertThat(info.getUsername(), is(equalTo("walter")));
		assertThat(info.getPassword(), is(equalTo("white")));
	}
}
