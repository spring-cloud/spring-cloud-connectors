package org.springframework.cloud.service;


public class BaseServiceInfo implements ServiceInfo {

	protected String id;

	public BaseServiceInfo(String id) {
		this.id = id;
	}
	
	@ServiceProperty
	public String getId() {
		return id;
	}
}