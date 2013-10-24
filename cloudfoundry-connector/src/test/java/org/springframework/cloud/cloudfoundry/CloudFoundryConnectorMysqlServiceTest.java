package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getMysqlServicePayload;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getMysqlServicePayloadWithLabelNoTags;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getMysqlServicePayloadWithLabelNoUri;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getServicesPayload;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;

public class CloudFoundryConnectorMysqlServiceTest extends AbstractCloudFactoryConnectorTest {
	@Test
	public void mysqlServiceCreation() {
		String[] versions = {"5.1", "5.5"};
		String name1 = "database-1";
		String name2 = "database-2";
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
								getMysqlServicePayload(version, "mysql-1", hostname, port, username, password, name1),
								getMysqlServicePayload(version, "mysql-2", hostname, port, username, password, name2)));
		}
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		
		MysqlServiceInfo info1 = (MysqlServiceInfo) getServiceInfo(serviceInfos, "mysql-1");
		MysqlServiceInfo info2 = (MysqlServiceInfo) getServiceInfo(serviceInfos, "mysql-2");
		assertNotNull(info1);
		assertNotNull(info2);
		assertEquals(getJdbcUrl("mysql", name1), info1.getJdbcUrl());
		assertEquals(getJdbcUrl("mysql", name2), info2.getJdbcUrl());
	}

	@Test
	public void mysqlServiceCreationWithLabelNoTags() {
		String[] versions = {"5.1", "5.5"};
		String name1 = "database-1";
		String name2 = "database-2";
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
								getMysqlServicePayloadWithLabelNoTags(version, "mysql-1", hostname, port, username, password, name1),
								getMysqlServicePayloadWithLabelNoTags(version, "mysql-2", hostname, port, username, password, name2)));
		}
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		
		MysqlServiceInfo info1 = (MysqlServiceInfo) getServiceInfo(serviceInfos, "mysql-1");
		MysqlServiceInfo info2 = (MysqlServiceInfo) getServiceInfo(serviceInfos, "mysql-2");
		assertNotNull(info1);
		assertNotNull(info2);
		assertEquals(getJdbcUrl("mysql", name1), info1.getJdbcUrl());
		assertEquals(getJdbcUrl("mysql", name2), info2.getJdbcUrl());
	}
	
	@Test
	public void mysqlServiceCreationWithLabelNoUri() {
		String[] versions = {"5.1", "5.5"};
		String name1 = "database-1";
		String name2 = "database-2";
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
								getMysqlServicePayloadWithLabelNoUri(version, "mysql-1", hostname, port, username, password, name1),
								getMysqlServicePayloadWithLabelNoUri(version, "mysql-2", hostname, port, username, password, name2)));
		}
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		
		MysqlServiceInfo info1 = (MysqlServiceInfo) getServiceInfo(serviceInfos, "mysql-1");
		MysqlServiceInfo info2 = (MysqlServiceInfo) getServiceInfo(serviceInfos, "mysql-2");
		assertNotNull(info1);
		assertNotNull(info2);
		assertEquals(getJdbcUrl("mysql", name1), info1.getJdbcUrl());
		assertEquals(getJdbcUrl("mysql", name2), info2.getJdbcUrl());
	}
}
