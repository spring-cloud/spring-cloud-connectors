package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.OracleServiceInfo;

public class LocalConfigConnectorOracleServiceTest extends AbstractLocalConfigConnectorWithUrisTest {

	@Test
	public void serviceCreation() {
		List<ServiceInfo> services = connector.getServiceInfos();
		ServiceInfo service = getServiceInfo(services, "oracle");
		assertNotNull(service);
		assertTrue(service instanceof OracleServiceInfo);
		assertUriParameters((OracleServiceInfo) service);
	}
}
