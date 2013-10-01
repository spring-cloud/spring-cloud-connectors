package org.springframework.cloud.cloudfoundry;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorTestHelper {
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static String getRedisServicePayload(String version, String serviceName,
			String hostname, int port,
			String password, String name) {
		String payload = readTestDataFile("test-redis-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$password", password);
		payload = payload.replace("$name", name);
		
		return payload;
	}

	public static String getMongoServicePayload(String version, String serviceName,
			String hostname, int port,
			String username, String password, String db, String name) {
		String payload = readTestDataFile("test-mongodb-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$username", username);
		payload = payload.replace("$password", password);
		payload = payload.replace("$db", db);
		payload = payload.replace("$name", name);
		
		return payload;
	}

	private static String getRelationalPayload(String templateFile, String version, String serviceName,
			                                   String hostname, int port,
			                                   String user, String password, String name) {
		String payload = readTestDataFile(templateFile);
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$user", user);
		payload = payload.replace("$password", password);
		payload = payload.replace("$name", name);
		
		return payload;
	}
	
	public static String getMysqlServicePayload(String version, String serviceName,
                                                String hostname, int port,
			                                    String user, String password, String name) {
		return getRelationalPayload("test-mysql-info.json", version, serviceName,
				                    hostname, port, user, password, name);
	}

	public static String getMysqlServicePayloadWithLabelNoTags(String version, String serviceName,
                                                               String hostname, int port,
                                                               String user, String password, String name) {
		return getRelationalPayload("test-mysql-info-with-label-no-tags.json", version, serviceName,
                                     hostname, port, user, password, name);
	}
	
	public static String getMysqlServicePayloadWithLabelNoUri(String version, String serviceName,
                                                              String hostname, int port,
                                                              String user, String password, String name) {
        return getRelationalPayload("test-mysql-info-with-label-no-uri.json", version, serviceName,
                                    hostname, port, user, password, name);
    }

	public static String getPostgresqlServicePayload(String version, String serviceName,
			                                         String hostname, int port,
			                                         String user, String password, String name) {
		return getRelationalPayload("test-postgresql-info.json", version, serviceName,
                                     hostname, port, user, password, name);
	}

	public static String getRabbitServicePayload(String version, String serviceName,
			String hostname, int port,
			String user, String password, String name,
			String vHost) {
		String payload = readTestDataFile("test-rabbit-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$user", user);
		payload = payload.replace("$pass", password);
		payload = payload.replace("$name", name);
		payload = payload.replace("$virtualHost", vHost);
		
		return payload;
	}

	public static String getMonitoringServicePayload(String serviceName) {
		String payload = readTestDataFile("test-monitoring-info.json");
		payload = payload.replace("$serviceName", serviceName);
		
		return payload;
	}

	public static String getServicesPayload(String... servicePayloads) {
		Map<String, List<String>> labelPayloadMap = new HashMap<String, List<String>>();
		
		for (String payload: servicePayloads) {
			String label = getServiceLabel(payload);
			
			List<String> payloadsForLabel = labelPayloadMap.get(label);
			if (payloadsForLabel == null) {
				payloadsForLabel = new ArrayList<String>();
				labelPayloadMap.put(label, payloadsForLabel);
			}
			payloadsForLabel.add(payload);
		}
		
		StringBuilder result = new StringBuilder("{\n");
		int labelSize = labelPayloadMap.size();
		int i = 0;

		for (Map.Entry<String, List<String>> entry : labelPayloadMap.entrySet()) {
			result.append(quote(entry.getKey()) + ":");
			result.append(getServicePayload(entry.getValue()));
			if (i++ != labelSize-1) {
				result.append(",\n");
			}
		}
		result.append("}");
		
		return result.toString();

	}
	
	public static String getApplicationInstanceInfo(String name, String... uri) {
		String payload = readTestDataFile("test-application-info.json");
		payload = payload.replace("$name", name);
		StringBuilder uris = new StringBuilder();
		for (String u : uri) {
			if (uris.length() > 0) {
				uris.append(",");
			}
			uris.append("\"");
			uris.append(u);
			uris.append("\"");
		}
		payload = payload.replace("$uris", uris.toString());
		
		return payload;
	}
	
	private static String getServicePayload(List<String> servicePayloads) {
		StringBuilder payload = new StringBuilder("[");
		
		// In Scala, this would have been servicePayloads mkString "," :-)
		for (int i = 0; i < servicePayloads.size(); i++) {
			payload.append(servicePayloads.get(i));
			if (i != servicePayloads.size() - 1) {
				payload.append(",");
			}
		}
		payload.append("]");
		
		return payload.toString();
	}
	
	private static String readTestDataFile(String fileName) {
		Scanner scanner = null;
		try {
			Reader fileReader = new InputStreamReader(CloudFoundryConnectorTestHelper.class.getResourceAsStream(fileName));
			scanner = new Scanner(fileReader);
			return scanner.useDelimiter("\\Z").next();
		} finally {
			scanner.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static String getServiceLabel(String servicePayload) {
		try {
			Map<String, Object> serviceMap = objectMapper.readValue(servicePayload, Map.class);
			return serviceMap.get("label").toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	private static String quote(String str) {
		return "\"" + str + "\"";
	}
}
