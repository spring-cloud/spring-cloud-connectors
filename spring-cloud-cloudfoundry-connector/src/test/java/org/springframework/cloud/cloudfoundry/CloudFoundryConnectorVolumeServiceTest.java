package org.springframework.cloud.cloudfoundry;

import java.util.List;

import org.junit.Test;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.NfsVolumeServiceInfo;
import org.springframework.cloud.service.common.SmbVolumeServiceInfo;
import org.springframework.cloud.service.common.VolumeServiceInfo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * 
 * @author Paul Warren
 * @author Dave Walter
 */
public class CloudFoundryConnectorVolumeServiceTest extends AbstractCloudFoundryConnectorTest {

	@Test
	public void volumeServiceInfoCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(
					getServicesPayload(
							getVolumeServiceBinding("test-nfs-volume-info.json","service-1", "/a/path", "r"),
							getVolumeServiceBinding("test-smb-volume-info.json","service-2", "/b/path", "rw")
			));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info1 = getServiceInfo(serviceInfos, "service-1");
		ServiceInfo info2 = getServiceInfo(serviceInfos, "service-2");

		assertServiceFoundOfType(info1, NfsVolumeServiceInfo.class);
		assertServiceFoundOfType(info2, SmbVolumeServiceInfo.class);

		assertEquals(1, (((VolumeServiceInfo)info1).getVolumeInfos().size()));
		assertEquals("/a/path", (((VolumeServiceInfo)info1).getVolumeInfos().stream().findFirst().get().getAbsolutePath()));

		assertEquals(1, (((VolumeServiceInfo)info2).getVolumeInfos().size()));
		assertEquals("/b/path", (((VolumeServiceInfo)info2).getVolumeInfos().stream().findFirst().get().getAbsolutePath()));
	}

	@Test
	public void volumeServiceInfoCreationWithLabelNoTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(
						getServicesPayload(
								getVolumeServiceBinding("test-nfs-volume-info-with-label-no-tags.json","service-1", "/a/path", "r"),
								getVolumeServiceBinding("test-smb-volume-info-with-label-no-tags.json","service-2", "/b/path", "rw")
						));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info1 = getServiceInfo(serviceInfos, "service-1");
		ServiceInfo info2 = getServiceInfo(serviceInfos, "service-2");

		assertServiceFoundOfType(info1, NfsVolumeServiceInfo.class);
		assertServiceFoundOfType(info2, SmbVolumeServiceInfo.class);

		assertEquals(1, (((VolumeServiceInfo)info1).getVolumeInfos().size()));
		assertEquals("/a/path", (((VolumeServiceInfo)info1).getVolumeInfos().stream().findFirst().get().getAbsolutePath()));

		assertEquals(1, (((VolumeServiceInfo)info2).getVolumeInfos().size()));
		assertEquals("/b/path", (((VolumeServiceInfo)info2).getVolumeInfos().stream().findFirst().get().getAbsolutePath()));
	}

	@Test
	public void volumeServiceInfoCreationWithNoLabelNoTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(
						getServicesPayload(
								getVolumeServiceBinding("test-volume-info-with-no-label-no-tags.json","service-1", "/a/path", "r"),
								getVolumeServiceBinding("test-volume-info-with-no-label-no-tags.json","service-2", "/b/path", "rw")
						));
		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		ServiceInfo info1 = getServiceInfo(serviceInfos, "service-1");
		ServiceInfo info2 = getServiceInfo(serviceInfos, "service-2");

		assertServiceFoundOfType(info1, BaseServiceInfo.class);
		assertServiceFoundOfType(info2, BaseServiceInfo.class);
	}

	private String getVolumeServiceBinding(String fileName, String serviceName, String containerDir, String mode) {
		String payload = readTestDataFile(fileName);
		payload = payload.replaceAll("\\$name", serviceName);
		payload = payload.replace("$containerDir", containerDir);
		payload = payload.replace("$mode", mode);
		return payload;
	}
}
