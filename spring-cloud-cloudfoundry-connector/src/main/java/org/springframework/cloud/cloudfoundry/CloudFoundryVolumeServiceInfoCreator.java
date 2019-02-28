package org.springframework.cloud.cloudfoundry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.service.common.VolumeServiceInfo;

/**
 * Provides an implementation of {@link CloudFoundryVolumeServiceInfoCreator} that detects CloudFoundry Volume service
 * tagged services bound to a Cloud Foundry deployed application.  This class will be discovered and
 * used automatically in a Spring Cloud Connector enabled application.
 *
 * @author Paul Warren
 * @author Dave Walter
 */
public abstract class CloudFoundryVolumeServiceInfoCreator<SI extends VolumeServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {

	public CloudFoundryVolumeServiceInfoCreator(Tags tags, String... uriSchemes) {
		super(tags, uriSchemes);
	}

	/**
	 * Parses CloudFoundry serviceData Map and returns a normalized VolumeServiceInfo instance
	 * to be used by Spring Cloud service connector creators.  This Cloud Foundry serviceData
	 * Map is created based on the VCAP_SERVICES environment variable.
	 *
	 * @param serviceData a java.util.Map containing the following required fields:  name,
	 *                    containerDir and mode
	 *
	 * @return {@link VolumeServiceInfo VolumeServiceInfo}
	 */
	@Override
	public SI createServiceInfo(Map<String, Object> serviceData) {

		String id = (String) serviceData.get("name");

		List<Map<String,String>> volumeMounts = (List<Map<String,String>>) serviceData.get("volume_mounts");

		Collection<File> mounts = new ArrayList<>();
		for (Map<String,String> volumeMount : volumeMounts) {
			File containerDir = new File(volumeMount.get("container_dir"));
			mounts.add(containerDir);
		}

		return (SI) newVolumeServiceInfo(id, Collections.unmodifiableCollection(mounts));
	}

	protected abstract VolumeServiceInfo newVolumeServiceInfo(String id, Collection<File> mounts);
}
