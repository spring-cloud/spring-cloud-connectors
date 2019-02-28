package org.springframework.cloud.cloudfoundry;

import java.io.File;
import java.util.Collection;

import org.springframework.cloud.service.common.SmbVolumeServiceInfo;
import org.springframework.cloud.service.common.VolumeServiceInfo;

/**
 * Concrete implementation of CloudFoundryVolumeServiceInfoCreator for SMB
 *
 * @author Paul Warren
 * @author Dave Walter
 */
public class SmbVolumeServiceInfoCreator extends CloudFoundryVolumeServiceInfoCreator<SmbVolumeServiceInfo> {

	public SmbVolumeServiceInfoCreator() {
		super(new Tags("smb"));
	}

	@Override
	protected VolumeServiceInfo newVolumeServiceInfo(String id, Collection<File> mounts) {
		return new SmbVolumeServiceInfo(id, mounts);
	}

}
