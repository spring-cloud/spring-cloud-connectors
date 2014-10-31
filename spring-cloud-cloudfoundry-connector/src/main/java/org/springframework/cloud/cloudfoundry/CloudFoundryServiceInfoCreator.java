package org.springframework.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;

/**
 * @author Ramnivas Laddad
 */
public abstract class CloudFoundryServiceInfoCreator<SI extends ServiceInfo> implements ServiceInfoCreator<SI, Map<String, Object>> {

	private Tags tags;
	private String[] uriSchemes;

	public CloudFoundryServiceInfoCreator(Tags tags, String... uriSchemes) {
		this.tags = tags;
		this.uriSchemes = uriSchemes;
	}

	public boolean accept(Map<String, Object> serviceData) {
		return tagsMatch(serviceData) || labelStartsWithTag(serviceData) || uriMatchesScheme(serviceData);
	}

	@SuppressWarnings("unchecked")
	protected boolean tagsMatch(Map<String, Object> serviceData) {
		List<String> serviceTags = (List<String>) serviceData.get("tags");
		return tags.containsOne(serviceTags);
	}

	protected boolean labelStartsWithTag(Map<String, Object> serviceData) {
		String label = (String) serviceData.get("label");
		return tags.startsWith(label);
	}

	protected boolean uriMatchesScheme(Map<String, Object> serviceData) {
		if (uriSchemes == null) {
			return false;
		}

		String uri = getUriFromCredentials(getCredentials(serviceData));
		if (uri != null) {
			for (String uriScheme : uriSchemes) {
				if (uri.startsWith(uriScheme + "://")) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> getCredentials(Map<String, Object> serviceData) {
		return (Map<String, Object>) serviceData.get("credentials");
	}

	protected String getUriFromCredentials(Map<String, Object> credentials) {
		return getStringFromCredentials(credentials, "uri", "url");
	}

	protected String getStringFromCredentials(Map<String, Object> credentials, String... keys) {
		for (String key : keys) {
			if (credentials.containsKey(key)) {
				return (String) credentials.get(key);
			}
		}
		return null;
	}

	protected int getIntFromCredentials(Map<String, Object> credentials, String... keys) {
		for (String key : keys) {
			if (credentials.containsKey(key)) {
				// allows the value to be quoted as a String or native integer type
				return Integer.parseInt(credentials.get(key).toString());
			}
		}
		return -1;
	}

	public String[] getUriSchemes() {
		return uriSchemes;
	}

	public String getDefaultUriScheme() {
		return uriSchemes[0];
	}
}
