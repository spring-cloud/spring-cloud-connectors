package org.springframework.cloud.cloudfoundry;

import java.io.File;
import java.util.Collection;

import org.springframework.cloud.service.common.NfsVolumeServiceInfo;
import org.springframework.cloud.service.common.VolumeServiceInfo;

/**
 * Concrete implementation of CloudFoundryVolumeServiceInfoCreator for NFS
 *
 * @author Paul Warren
 * @author Dave Walter
 */
public class NfsVolumeServiceInfoCreator extends CloudFoundryVolumeServiceInfoCreator<NfsVolumeServiceInfo> {

	public NfsVolumeServiceInfoCreator() {
		super(new Tags("nfs"));
	}

	@Override
	protected VolumeServiceInfo newVolumeServiceInfo(String id, Collection<File> mounts) {
		return new NfsVolumeServiceInfo(id, mounts);
	}

}
