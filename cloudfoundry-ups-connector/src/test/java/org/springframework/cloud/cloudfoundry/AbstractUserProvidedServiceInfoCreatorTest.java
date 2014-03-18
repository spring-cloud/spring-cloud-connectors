package org.springframework.cloud.cloudfoundry;

public class AbstractUserProvidedServiceInfoCreatorTest extends AbstractCloudFoundryConnectorRelationalServiceTest {
	protected String getUserProvidedServicePayload(String serviceName, String hostname, int port,
												   String user, String password, String name, String scheme) {
		String payload = getRelationalPayload("test-ups-info.json", "", serviceName,
				hostname, port, user, password, name);
		return payload.replace("$scheme", scheme);
	}

	protected String getUserProvidedServicePayloadWithNoUri(String serviceName, String hostname, int port,
															String user, String password, String name) {
		return getRelationalPayload("test-ups-info-no-uri.json", "", serviceName,
				hostname, port, user, password, name);
	}
}
