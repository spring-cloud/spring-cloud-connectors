package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.MysqlServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class MysqlServiceInfoCreator extends RelationalServiceInfoCreator<MysqlServiceInfo> {

	public MysqlServiceInfoCreator() {
        // the literal in the tag is CloudFoundry-specific
		super(new Tags("mysql"), MysqlServiceInfo.URI_SCHEME);
	}

	@Override
	public MysqlServiceInfo createServiceInfo(String id, String url) {
		return new MysqlServiceInfo(id, url);
	}
}
