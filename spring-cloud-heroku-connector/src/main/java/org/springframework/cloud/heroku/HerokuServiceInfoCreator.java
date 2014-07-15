package org.springframework.cloud.heroku;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfoCreator;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public abstract class HerokuServiceInfoCreator<SI extends ServiceInfo> extends UriBasedServiceInfoCreator<SI> {

	public HerokuServiceInfoCreator(String uriScheme) {
		super(uriScheme);
	}

	/**
	 * Get prefixes for env variable with which the associated {@link ServiceInfo} may be created.
	 *
	 * Unlike CloudFoundry which exposes VCAP_SERVICES as a single environment to encompass all services bound
	 * to the app, Heroku expose one environment variable per app. This method allows each info creator to declare
	 * appropriate env variables.
	 *
	 * @return prefixes for the relevant environment variables
	 */
	public abstract String[] getEnvPrefixes();
}
