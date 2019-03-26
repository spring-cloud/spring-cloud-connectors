package org.springframework.cloud.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.service.BaseServiceInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CloudFoundryServiceInfoCreatorTest {

	@Test
	public void tagsMatch() {
		DummyServiceInfoCreator serviceInfoCreator = new DummyServiceInfoCreator(new Tags("firstTag", "secondTag"));

		assertAcceptedWithTags(serviceInfoCreator, "firstTag", "noMatchTag");
		assertAcceptedWithTags(serviceInfoCreator, "noMatchTag", "secondTag");
		assertAcceptedWithTags(serviceInfoCreator, "firstTag", "secondTag");

		assertNotAcceptedWithTags(serviceInfoCreator, "noMatchTag");
		assertNotAcceptedWithTags(serviceInfoCreator);
	}

	@Test
	public void labelMatches() {
		DummyServiceInfoCreator serviceInfoCreator = new DummyServiceInfoCreator(new Tags("testTag"));

		assertAcceptedWithLabel(serviceInfoCreator, "testTag");
		assertAcceptedWithLabel(serviceInfoCreator, "testTagWithSuffix");

		assertNotAcceptedWithLabel(serviceInfoCreator, "withPrefixTestTag");
		assertNotAcceptedWithLabel(serviceInfoCreator, "noMatchTag");
	}

	@Test
	public void uriSchemeMatches() {
		DummyServiceInfoCreator serviceInfoCreator = new DummyServiceInfoCreator(new Tags(), "amqp", "amqps");

		assertAcceptedWithCredentials(serviceInfoCreator, "uri", "amqp://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "uri", "amqps://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "url", "amqp://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "url", "amqps://example.com");

		assertNotAcceptedWithCredentials(serviceInfoCreator, "uri", "https://example.com");
		assertNotAcceptedWithCredentials(serviceInfoCreator, "url", "https://example.com");
		assertNotAcceptedWithCredentials(serviceInfoCreator, "otherkey", "amqp://example.com");
		assertNotAcceptedWithCredentials(serviceInfoCreator, "otherkey", "amqps://example.com");
	}

	@Test
	public void uriKeyMatchesScheme() {
		DummyServiceInfoCreator serviceInfoCreator = new DummyServiceInfoCreator(new Tags(), "amqp", "amqps");

		assertAcceptedWithCredentials(serviceInfoCreator, "amqpUri", "https://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "amqpsUri", "https://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "amqpUrl", "https://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "amqpsUrl", "https://example.com");

		assertAcceptedWithCredentials(serviceInfoCreator, "amqpuri", "https://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "amqpsuri", "https://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "amqpurl", "https://example.com");
		assertAcceptedWithCredentials(serviceInfoCreator, "amqpsurl", "https://example.com");
	}

	@Test
	public void uriFromCredentials() {
		DummyServiceInfoCreator serviceInfoCreator = new DummyServiceInfoCreator(new Tags(), "amqp", "amqps");

		assertUriRetrieved(serviceInfoCreator, "uri", "amqp://example.com");
		assertUriRetrieved(serviceInfoCreator, "uri", "amqps://example.com");
		assertUriRetrieved(serviceInfoCreator, "url", "amqp://example.com");
		assertUriRetrieved(serviceInfoCreator, "url", "amqps://example.com");

		assertUriRetrieved(serviceInfoCreator, "amqpUri", "https://example.com");
		assertUriRetrieved(serviceInfoCreator, "amqpsUri", "https://example.com");
		assertUriRetrieved(serviceInfoCreator, "amqpUrl", "https://example.com");
		assertUriRetrieved(serviceInfoCreator, "amqpsUrl", "https://example.com");

		assertUriRetrieved(serviceInfoCreator, "amqpuri", "https://example.com");
		assertUriRetrieved(serviceInfoCreator, "amqpsuri", "https://example.com");
		assertUriRetrieved(serviceInfoCreator, "amqpurl", "https://example.com");
		assertUriRetrieved(serviceInfoCreator, "amqpsurl", "https://example.com");
	}


	@Test
	public void uriFromCredentialsWithNoSchemes() {
		DummyServiceInfoCreator serviceInfoCreator = new DummyServiceInfoCreator(new Tags());

		assertUriRetrieved(serviceInfoCreator, "uri", "amqp://example.com");
		assertUriRetrieved(serviceInfoCreator, "url", "amqp://example.com");
	}

	private void assertUriRetrieved(DummyServiceInfoCreator serviceInfoCreator, String key, String value) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withCredentials(key, value).build();

		Map<String, Object> credentials = serviceInfoCreator.getCredentials(serviceData);
		String uri = serviceInfoCreator.getUriFromCredentials(credentials);

		assertEquals(value, uri);
	}

	private void assertAcceptedWithTags(DummyServiceInfoCreator serviceInfoCreator, String... tags) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withTags(tags).build();
		assertTrue(serviceInfoCreator.accept(serviceData));
	}

	private void assertNotAcceptedWithTags(DummyServiceInfoCreator serviceInfoCreator, String... tags) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withTags(tags).build();
		assertFalse(serviceInfoCreator.accept(serviceData));
	}

	private void assertAcceptedWithLabel(DummyServiceInfoCreator serviceInfoCreator, String label) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withLabel(label).build();
		assertTrue(serviceInfoCreator.accept(serviceData));
	}

	private void assertNotAcceptedWithLabel(DummyServiceInfoCreator serviceInfoCreator, String label) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withLabel(label).build();
		assertFalse(serviceInfoCreator.accept(serviceData));
	}

	private void assertAcceptedWithCredentials(DummyServiceInfoCreator serviceInfoCreator, String key, String value) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withCredentials(key, value).build();
		assertTrue(serviceInfoCreator.accept(serviceData));
	}

	private void assertNotAcceptedWithCredentials(DummyServiceInfoCreator serviceInfoCreator, String key, String value) {
		Map<String, Object> serviceData = new ServiceDataBuilder().withCredentials(key, value).build();
		assertFalse(serviceInfoCreator.accept(serviceData));
	}

	private class ServiceDataBuilder {
		private String[] tags = new String[0];
		private String label = "";
		private Map<String, Object> credentials = new HashMap<String, Object>();

		public ServiceDataBuilder withTags(String... tags) {
			this.tags = tags;
			return this;
		}

		public ServiceDataBuilder withCredentials(String key, String value) {
			credentials.put(key, value);
			return this;
		}

		public ServiceDataBuilder withLabel(String label) {
			this.label = label;
			return this;
		}

		public Map<String, Object> build() {
			Map<String, Object> serviceData = new HashMap<String, Object>();

			serviceData.put("tags", Arrays.asList(tags));
			serviceData.put("label", label);
			serviceData.put("credentials", credentials);

			return serviceData;
		}
	}

	private class DummyServiceInfoCreator extends CloudFoundryServiceInfoCreator<DummyServiceInfo> {
		public DummyServiceInfoCreator(Tags tags) {
			super(tags);
		}

		public DummyServiceInfoCreator(Tags tags, String... uriSchemes) {
			super(tags, uriSchemes);
		}

		@Override
		public DummyServiceInfo createServiceInfo(Map<String, Object> serviceData) {
			return new DummyServiceInfo("test");
		}
	}

	private class DummyServiceInfo extends BaseServiceInfo {
		public DummyServiceInfo(String id) {
			super(id);
		}

		@Override
		public String getId() {
			return null;
		}
	}
}