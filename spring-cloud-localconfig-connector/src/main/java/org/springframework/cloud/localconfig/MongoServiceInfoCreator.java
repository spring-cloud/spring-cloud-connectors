package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.common.MongoServiceInfo;

/**
 * @author Christopher Smith
 */
public class MongoServiceInfoCreator extends LocalConfigServiceInfoCreator<MongoServiceInfo> {

	public MongoServiceInfoCreator() {
		super(MongoServiceInfo.MONGODB_SCHEME);
	}

	@Override
	public MongoServiceInfo createServiceInfo(String id, String uri) {
		return new MongoServiceInfo(id, uri);
	}
}
