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

package org.springframework.cloud.localconfig;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.HANAServiceInfo;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Johannes Roesch
 */
public class LocalConfigConnectorHANAServiceTest
		extends AbstractLocalConfigConnectorWithUrisTest {

	@Test public void serviceCreation() {
		List<ServiceInfo> services = connector.getServiceInfos();
		ServiceInfo service = getServiceInfo(services, "hana");
		assertNotNull(service);
		assertTrue(service instanceof HANAServiceInfo);
		assertUriParameters((HANAServiceInfo) service);
	}

}
