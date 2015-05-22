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
		super(new Tags("mongodb"), MongoServiceInfo.MONGODB_SCHEME);
	}

	public MongoServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		String id = getId(serviceData);

		String uri = getUriFromCredentials(getCredentials(serviceData));

		return new MongoServiceInfo(id, uri);
	}

}
