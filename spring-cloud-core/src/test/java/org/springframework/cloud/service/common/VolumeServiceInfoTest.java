package org.springframework.cloud.service.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Paul Warren
 * @author Dave Walter
 */
public class VolumeServiceInfoTest {
	@Test
	public void nfsAllArgs() {
		File file1 = new File("/a/path");
		File file2 = new File("/b/path");
		Collection<File> volumes = new ArrayList<>();
		volumes.add(file1);
		volumes.add(file2);

		NfsVolumeServiceInfo serviceInfo = new NfsVolumeServiceInfo("id", volumes);

		assertEquals(2, serviceInfo.getVolumeInfos().size());
		assertThat(serviceInfo.getVolumeInfos(), hasItems(file1, file2));
	}

	@Test
	public void smbAllArgs() {
		File file1 = new File("/a/path");
		File file2 = new File("/b/path");
		Collection<File> volumes = new ArrayList<>();
		volumes.add(file1);
		volumes.add(file2);

		SmbVolumeServiceInfo serviceInfo = new SmbVolumeServiceInfo("id", volumes);

		assertEquals(2, serviceInfo.getVolumeInfos().size());
		assertThat(serviceInfo.getVolumeInfos(), hasItems(file1, file2));
	}

	@Test(expected=IllegalArgumentException.class)
	public void nullVolumeInfo() {
		new NfsVolumeServiceInfo("id",  null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingVolumeInfo() {
		new NfsVolumeServiceInfo("id",  new ArrayList<>());
	}
}
