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
	private String uriScheme;

	public CloudFoundryServiceInfoCreator(String tag, String uriScheme) {
		this.tag = tag;
		this.uriScheme = uriScheme;
	}

    public CloudFoundryServiceInfoCreator(String tag) {
        this(tag, null);
    }
    
	@SuppressWarnings("unchecked")
	public boolean accept(Map<String,Object> serviceData) {
		List<String> tags = (List<String>)serviceData.get("tags");
		String label = (String) serviceData.get("label");
		
		boolean tagAcceptable = tags != null && tags.contains(tag);
		// Use label as a tag to cover cases where tag doesn't exist and label value
		// itself starts with the tag text (for example, "label : mysql-n/a")
		boolean labelAcceptable = label != null && label.startsWith(tag);
		
		return tagAcceptable || labelAcceptable || isUriAcceptable(serviceData);
	}
	
	private boolean isUriAcceptable(Map<String,Object> serviceData) {
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
	
	protected String getTag() {
		return tag;
	}
}
