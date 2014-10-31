package org.springframework.cloud.cloudfoundry;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MongoServiceInfo;

/**
 * @author Ramnivas Laddad
 */
public class CloudFoundryConnectorMongodbServiceTest extends AbstractCloudFoundryConnectorTest {
	@Test
	public void mongoServiceCreation() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getMongoServicePayload("mongo-1", hostname, port, username, password, "inventory-1", "db"),
						getMongoServicePayload("mongo-2", hostname, port, username, password, "inventory-2", "db")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "mongo-1", MongoServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "mongo-2", MongoServiceInfo.class);
	}

	@Test
	public void mongoServiceCreationNoLabelNoTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getMongoServicePayloadNoLabelNoTags("mongo-1", hostname, port, username, password, "inventory-1", "db"),
						getMongoServicePayloadNoLabelNoTags("mongo-2", hostname, port, username, password, "inventory-2", "db")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, "mongo-1", MongoServiceInfo.class);
		assertServiceFoundOfType(serviceInfos, "mongo-2", MongoServiceInfo.class);
	}

	private String getMongoServicePayload(String serviceName, String hostname, int port,
										  String username, String password, String db, String name) {
		return getMongoServicePayload("test-mongodb-info.json",
				serviceName, hostname, port, username, password, db, name);
	}

	private String getMongoServicePayloadNoLabelNoTags(String serviceName,
													   String hostname, int port,
													   String username, String password, String db, String name) {
		return getMongoServicePayload("test-mongodb-info-no-label-no-tags.json",
				serviceName, hostname, port, username, password, db, name);
	}

	private String getMongoServicePayload(String payloadFile, String serviceName,
										  String hostname, int port,
										  String username, String password, String db, String name) {
		String payload = readTestDataFile(payloadFile);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$username", username);
		payload = payload.replace("$password", password);
		payload = payload.replace("$db", db);
		payload = payload.replace("$name", name);

		return payload;
	}
}
