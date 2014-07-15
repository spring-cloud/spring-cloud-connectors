package org.springframework.cloud.service;

public class UriBasedServiceData {
	private final String key;
	private final String uri;

	public UriBasedServiceData(String key, String uri) {
		this.key = key;
		this.uri = uri;
	}

	public String getKey() {
		return key;
	}

	public String getUri() {
		return uri;
	}
}