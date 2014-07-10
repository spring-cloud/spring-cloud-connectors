package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.MongoServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class MongoServiceInfoCreator extends CloudFoundryServiceInfoCreator<MongoServiceInfo> {

	public MongoServiceInfoCreator() {
	    // the literal in the tag is CloudFoundry-specific
		super(new Tags("mongodb"), MongoServiceInfo.URI_SCHEME);
	}

	public MongoServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");

		String id = (String) serviceData.get("name");

		String uri = getStringFromCredentials(credentials, "uri", "url");

		return new MongoServiceInfo(id, uri);
	}

}
