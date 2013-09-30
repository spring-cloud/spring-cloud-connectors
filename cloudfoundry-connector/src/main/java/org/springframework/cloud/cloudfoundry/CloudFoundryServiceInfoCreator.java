package org.springframework.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.ServiceInfoCreator;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class CloudFoundryServiceInfoCreator<SI extends ServiceInfo> implements ServiceInfoCreator<SI, Map<String,Object>> {

	private String tag;

	public CloudFoundryServiceInfoCreator(String tag) {
		this.tag = tag;
	}
	
	@SuppressWarnings("unchecked")
	public boolean accept(Map<String,Object> serviceData) {
		List<String> tags = (List<String>)serviceData.get("tags");
		String label = (String) serviceData.get("label");
		
		boolean tagAcceptable = tags.contains(tag);
		// Use label as a tag to cover cases where tag doesn't exist and label value
		// itself starts with the tag text (for example, "label : mysql-n/a")
		boolean labelAcceptable = label != null && label.startsWith(tag);
		
		return tagAcceptable || labelAcceptable;
	}
}
