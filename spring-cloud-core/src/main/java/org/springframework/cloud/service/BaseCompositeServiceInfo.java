package org.springframework.cloud.service;

import java.util.Arrays;
import java.util.List;

/**
 * A generic implementation of {@link CompositeServiceInfo} that should suffice in many situations.
 * 
 * @author Ramnivas Laddad
 *
 */
public class BaseCompositeServiceInfo extends BaseServiceInfo implements CompositeServiceInfo {

	private List<ServiceInfo> constituents;

	public BaseCompositeServiceInfo(String id, ServiceInfo... constituents) {
		super(id);
		this.constituents = Arrays.asList(constituents);
	}

	@Override
	public List<ServiceInfo> getServiceInfos() {
		return constituents;
	}
}
