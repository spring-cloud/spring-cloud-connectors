package org.springframework.cloud.localconfig;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.DB2ServiceInfo;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LocalConfigConnectorDB2ServiceTest extends AbstractLocalConfigConnectorWithUrisTest {

	@Test
	public void serviceCreation() {
		List<ServiceInfo> services = connector.getServiceInfos();
		ServiceInfo service = getServiceInfo(services, "db2");
		assertNotNull(service);
		assertTrue(service instanceof DB2ServiceInfo);
		assertUriParameters((DB2ServiceInfo) service);
	}

}
