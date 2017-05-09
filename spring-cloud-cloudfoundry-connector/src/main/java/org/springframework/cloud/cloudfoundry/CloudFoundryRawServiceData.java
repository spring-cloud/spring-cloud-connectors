package org.springframework.cloud.cloudfoundry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link CloudFoundryRawServiceData} object represents the data read from the {@literal VCAP_SERVICES}
 * environment variable and transformed from JSON text to a collection of objects.
 *
 * The root of the data structure is a {@link List}. Each element of the list represents one service from
 * the JSON.
 */
public class CloudFoundryRawServiceData extends HashMap<String, List<Map<String,Object>>> {
	public CloudFoundryRawServiceData() {
		super();
	}

	public CloudFoundryRawServiceData(Map<? extends String, ? extends List<Map<String, Object>>> map) {
		super(map);
	}
}
