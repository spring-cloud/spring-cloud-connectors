package org.springframework.cloud.service.common;

import java.io.File;
import java.util.Collection;

/**
 * @author Paul Warren
 * @author Dave Walter
 */
public class NfsVolumeServiceInfo extends VolumeServiceInfo {

	public NfsVolumeServiceInfo(String id, Collection<File> mounts) {
		super(id, mounts);
	}
}
