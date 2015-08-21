package org.springframework.cloud.cloudfoundry;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Base test class that provides setup and utility methods to generate test payload
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractCloudFoundryConnectorTest {
	protected CloudFoundryConnector testCloudConnector = new CloudFoundryConnector();
	@Mock protected EnvironmentAccessor mockEnvironment;

	protected static final String hostname = "10.20.30.40";
	protected static final int port = 1234;
	protected static String username = "myuser";
	protected static final String password = "mypass";

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testCloudConnector.setCloudEnvironment(mockEnvironment);
	}
	
	protected static ServiceInfo getServiceInfo(List<ServiceInfo> serviceInfos, String serviceId) {
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getId().equals(serviceId)) {
				return serviceInfo;
			}
		}
		return null;
	}

	protected String readTestDataFile(String fileName) {
		Scanner scanner = null;
		try {
			Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(fileName));
			scanner = new Scanner(fileReader);
			return scanner.useDelimiter("\\Z").next();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
	
	protected static String getServicesPayload(String... servicePayloads) {
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
			result.append(quote(entry.getKey())).append(":");
			result.append(getServicePayload(entry.getValue()));
			if (i++ != labelSize-1) {
				result.append(",\n");
			}
		}
		result.append("}");
		
		return result.toString();

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
	
	@SuppressWarnings("unchecked")
	private static String getServiceLabel(String servicePayload) {
		try {
			Map<String, Object> serviceMap = objectMapper.readValue(servicePayload, Map.class);
			return serviceMap.get("label").toString();
		} catch (Exception e) {
			return null;
		} 
	}
	
	private static String quote(String str) {
		return "\"" + str + "\"";
	}

	protected static void assertServiceFoundOfType(ServiceInfo serviceInfo, Class<? extends ServiceInfo> type) {
		assertNotNull(serviceInfo);
		assertThat(serviceInfo, instanceOf(type));
	}

	protected static void assertServiceFoundOfType(List<ServiceInfo> serviceInfos, String serviceId, Class<? extends ServiceInfo> type) {
		ServiceInfo serviceInfo = getServiceInfo(serviceInfos, serviceId);
		assertServiceFoundOfType(serviceInfo, type);
	}

	protected static void assertUriBasedServiceInfoFields(ServiceInfo serviceInfo,
														  String scheme, String host, int port,
														  String username, String password, String path) {
		assertThat(serviceInfo, instanceOf(UriBasedServiceInfo.class));

		UriBasedServiceInfo info = (UriBasedServiceInfo) serviceInfo;

		assertEquals(scheme, info.getScheme());
		assertEquals(host, info.getHost());
		assertEquals(port, info.getPort());
		assertEquals(username, info.getUserName());
		assertEquals(password, info.getPassword());
		assertEquals(path, info.getPath());
	}
}
