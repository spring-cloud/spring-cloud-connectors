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
	private String uriScheme;

	public CloudFoundryServiceInfoCreator(Tags tags, String uriScheme) {
		this.tags = tags;
		this.uriScheme = uriScheme;
	}

	public CloudFoundryServiceInfoCreator(Tags tags) {
		this(tags, null);
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
		if (uriScheme == null) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Map<String, String> credentials = (Map<String, String>) serviceData.get("credentials");
		if (credentials != null) {
			String uri = credentials.get("uri");
			if (uri == null) {
				uri = credentials.get("url");
			}
			if (uri != null) {
				return uri.startsWith(uriScheme + "://");
			}
		}
		return false;
	}

	public String getUriScheme() {
		return uriScheme;
	}
}
