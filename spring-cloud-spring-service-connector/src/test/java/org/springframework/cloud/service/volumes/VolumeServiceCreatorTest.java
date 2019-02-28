package org.springframework.cloud.service.volumes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.NfsVolumeServiceInfo;
import org.springframework.cloud.service.common.SmbVolumeServiceInfo;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VolumeServiceCreatorTest {

    private NfsVolumeServiceCreator nfsTestCreator = new NfsVolumeServiceCreator();
    private SmbVolumeServiceCreator smbTestCreator = new SmbVolumeServiceCreator();

    @Test
    public void nfsVolumeServiceNoConfig() throws Exception {
        File file1 = new File("/a/path");
        File file2 = new File("/b/path");
        Collection<File> volumes = new ArrayList<>();
        volumes.add(file1);
        volumes.add(file2);

        NfsVolumeServiceInfo serviceInfo = new NfsVolumeServiceInfo("id", volumes);

        NfsVolumes nfsVolumeServiceInfo = nfsTestCreator.create(serviceInfo, null);
        assertNotNull(nfsVolumeServiceInfo);

        assertEquals(2, serviceInfo.getVolumeInfos().size());
        assertThat(serviceInfo.getVolumeInfos(), hasItems(file1, file2));
    }

    @Test
    public void nfsVolumeServiceConfigIsIgnored() throws Exception {
        File file1 = new File("/a/path");
        File file2 = new File("/b/path");
        Collection<File> volumes = new ArrayList<>();
        volumes.add(file1);
        volumes.add(file2);

        NfsVolumeServiceInfo serviceInfo = new NfsVolumeServiceInfo("id", volumes);

        NfsVolumes nfsVolumeServiceInfo = nfsTestCreator.create(serviceInfo, new ServiceConnectorConfig() {});
        assertNotNull(nfsVolumeServiceInfo);

        assertEquals(2, serviceInfo.getVolumeInfos().size());
        assertThat(serviceInfo.getVolumeInfos(), hasItems(file1, file2));
    }

    @Test
    public void smbVolumeServiceNoConfig() throws Exception {
        File file1 = new File("/a/path");
        File file2 = new File("/b/path");
        Collection<File> volumes = new ArrayList<>();
        volumes.add(file1);
        volumes.add(file2);

        SmbVolumeServiceInfo serviceInfo = new SmbVolumeServiceInfo("id", volumes);

        SmbVolumes smbVolumeServiceInfo = smbTestCreator.create(serviceInfo, null);
        assertNotNull(smbVolumeServiceInfo);

        assertEquals(2, serviceInfo.getVolumeInfos().size());
        assertThat(serviceInfo.getVolumeInfos(), hasItems(file1, file2));
    }

    @Test
    public void smbVolumeServiceConfigIsIgnored() throws Exception {
        File file1 = new File("/a/path");
        File file2 = new File("/b/path");
        Collection<File> volumes = new ArrayList<>();
        volumes.add(file1);
        volumes.add(file2);

        SmbVolumeServiceInfo serviceInfo = new SmbVolumeServiceInfo("id", volumes);

        SmbVolumes smbVolumeServiceInfo = smbTestCreator.create(serviceInfo, new ServiceConnectorConfig() {});
        assertNotNull(smbVolumeServiceInfo);

        assertEquals(2, serviceInfo.getVolumeInfos().size());
        assertThat(serviceInfo.getVolumeInfos(), hasItems(file1, file2));
    }
}