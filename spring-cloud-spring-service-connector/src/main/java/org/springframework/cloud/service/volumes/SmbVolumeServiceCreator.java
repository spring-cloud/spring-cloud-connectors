package org.springframework.cloud.service.volumes;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.NfsVolumeServiceInfo;
import org.springframework.cloud.service.common.SmbVolumeServiceInfo;

/**
 * @author Paul Warren
 * @author Dave Walter
 */
public class SmbVolumeServiceCreator
        extends AbstractServiceConnectorCreator<SmbVolumes, SmbVolumeServiceInfo> {

    @Override
    public SmbVolumes create(SmbVolumeServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
        SmbVolumes volumes = new SmbVolumes();
        volumes.addAll(serviceInfo.getVolumeInfos());
        return volumes;
    }
}