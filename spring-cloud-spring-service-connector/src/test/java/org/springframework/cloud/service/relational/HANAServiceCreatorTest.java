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

package org.springframework.cloud.service.relational;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.HANAServiceInfo;

import static org.mockito.Mockito.when;

/**
 * @author Johannes Roesch
 */
public class HANAServiceCreatorTest
		extends AbstractDataSourceCreatorTest<HANADataSourceCreator, HANAServiceInfo> {
	public static final String TEST_HANA_DRIVER = "com.sap.example.Driver";

	@Mock private HANAServiceInfo mockDB2ServiceInfo;

	@Before public void setup() {
		MockitoAnnotations.initMocks(this);
		// set a dummy JDBC driver since we can't yet include a real DB2 driver in the project due to licensing restrictions
		System.setProperty("spring-cloud.hana.driver", TEST_HANA_DRIVER);
	}

	@Override public HANAServiceInfo createServiceInfo() {
		when(mockDB2ServiceInfo.getJdbcUrl()).thenReturn(
				"jdbc:sap://10.20.30.40:50000/currentschema=database-123&user=myuser&password=mypassword");
		when(mockDB2ServiceInfo.getUri())
				.thenReturn("sap://myuser:mypass@10.20.30.40:50000/database-123");

		return mockDB2ServiceInfo;
	}

	@Override public String getDriverName() {
		return TEST_HANA_DRIVER;
	}

	@Override public HANADataSourceCreator getCreator() {
		return new HANADataSourceCreator();
	}

	@Override public String getValidationQueryStart() {
		return "VALUES 1";
	}
}
