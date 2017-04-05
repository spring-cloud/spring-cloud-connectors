package org.springframework.cloud.localconfig;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.SqlServerServiceInfo;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LocalConfigConnectorSqlServerServiceTest extends AbstractLocalConfigConnectorWithUrisTest {

	@Test
	public void serviceCreation() {
		List<ServiceInfo> services = connector.getServiceInfos();
		ServiceInfo service = getServiceInfo(services, "sql");
		assertNotNull(service);
		assertTrue(service instanceof SqlServerServiceInfo);
		assertUriParameters((SqlServerServiceInfo) service);
	}

}
