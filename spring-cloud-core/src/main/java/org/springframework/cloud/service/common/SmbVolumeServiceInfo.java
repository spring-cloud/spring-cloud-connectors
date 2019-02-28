package org.springframework.cloud.service.common;

import java.io.File;
import java.util.Collection;

/**
 * @author Paul Warren
 * @author Dave Walter
 */
public class SmbVolumeServiceInfo extends VolumeServiceInfo {

	public SmbVolumeServiceInfo(String id, Collection<File> mounts) {
		super(id, mounts);
	}
}
