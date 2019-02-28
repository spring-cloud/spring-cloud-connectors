package org.springframework.cloud.service.volumes;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.NfsVolumeServiceInfo;

import static java.lang.String.format;

/**
 * @author Paul Warren
 * @author Dave Walter
 */
public class NfsVolumeServiceCreator
        extends AbstractServiceConnectorCreator<NfsVolumes, NfsVolumeServiceInfo> {

    @Override
    public NfsVolumes create(NfsVolumeServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
        NfsVolumes volumes = new NfsVolumes();
        volumes.addAll(serviceInfo.getVolumeInfos());
        return volumes;
    }
}