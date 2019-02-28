package org.springframework.cloud.service.common;

import java.io.File;
import java.util.Collection;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * @author Paul Warren
 * @author Dave Walter
 */
public abstract class VolumeServiceInfo extends BaseServiceInfo {

    private Collection<File> mounts;

    public VolumeServiceInfo(String id, Collection<File> mounts) {
        super(id);
        if (mounts == null) {
            throw new IllegalArgumentException("Missing mounts");
        }
        if (mounts.isEmpty()) {
            throw new IllegalArgumentException("Missing mounts");
        }
        this.mounts = mounts;
    }

    @ServiceProperty
    public Collection<File> getVolumeInfos() {
        return mounts;
    }
}