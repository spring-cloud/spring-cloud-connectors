package org.springframework.cloud.heroku;

import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends HerokuServiceInfoCreator<SI> {
	public RelationalServiceInfoCreator(String urlProtocol) {
		super(urlProtocol);
	}
}
