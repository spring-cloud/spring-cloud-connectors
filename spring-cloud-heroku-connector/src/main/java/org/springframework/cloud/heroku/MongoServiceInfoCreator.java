package org.springframework.cloud.heroku;

import org.springframework.cloud.service.common.MongoServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class MongoServiceInfoCreator extends HerokuServiceInfoCreator<MongoServiceInfo> {

	public MongoServiceInfoCreator() {
		super(MongoServiceInfo.MONGODB_SCHEME);
	}

	@Override
	public MongoServiceInfo createServiceInfo(String id, String uri) {
		return new MongoServiceInfo(HerokuUtil.computeServiceName(id), uri);
	}

	@Override
	public String[] getEnvPrefixes() {
		return new String[]{ "MONGODB_URI", "MONGOLAB_URI", "MONGOHQ_URL", "MONGOSOUP_URL" };
	}
}
