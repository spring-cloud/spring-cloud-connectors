package org.springframework.cloud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.test.CloudTestUtil;
import org.springframework.cloud.test.CloudTestUtil.StubApplicationInstanceInfo;
import org.springframework.cloud.test.CloudTestUtil.StubCloudConnector;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudTest {

	private List<ServiceConnectorCreator<?, ? extends ServiceInfo>> serviceConnectorCreators;
	
	@Before
	public void setup() {
		serviceConnectorCreators = new ArrayList<ServiceConnectorCreator<?, ? extends ServiceInfo>>();
	}
	
	@Test
	public void appProps() {
		Map<String, Object> appProps = new HashMap<String, Object>();
		appProps.put("foo", "bar");
		appProps.put("users", null); // on v2, users property is null
		StubCloudConnector stubCloudConnector = 
				CloudTestUtil.getTestCloudConnector(new StubApplicationInstanceInfo("instance-id-1", "myapp", appProps));
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		assertEquals("myapp", testCloud.getCloudProperties().get("cloud.application.app-id"));
		assertEquals("instance-id-1", testCloud.getCloudProperties().get("cloud.application.instance-id"));
		assertEquals("bar", testCloud.getCloudProperties().get("cloud.application.foo"));
	}
	
	@Test
	public void serviceConnectorCreationDefaultTypeAndConfig() {
		StubServiceInfo testServiceInfo = new StubServiceInfo("test-id", "test-host", 1000, "test-username", "test-password");
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo);
		serviceConnectorCreators.add(new StubServiceConnectorCreator());
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		StubServiceConnector connector = testCloud.getServiceConnector(testServiceInfo.getId(), null, null);
		
		assertStubService(testServiceInfo, connector, null);
	}
	
	@Test
	public void getSingletonServiceConnectorSingleService() {
		StubServiceInfo testServiceInfo = new StubServiceInfo("test-id", "test-host", 1000, "test-username", "test-password");
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo);
		serviceConnectorCreators.add(new StubServiceConnectorCreator());
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		StubServiceConnector connector = testCloud.getSingletonServiceConnector(null, null);
		
		assertStubService(testServiceInfo, connector, null);
	}
	
	@Test(expected=CloudException.class)
	public void getSingletonServiceConnectorNoService() {
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector();
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		testCloud.getSingletonServiceConnector(null, null);
	}

	@Test(expected=CloudException.class)
	public void getSingletonServiceConnectorMultipleServices() {
		StubServiceInfo testServiceInfo1 = new StubServiceInfo("test-id", "test-host", 1000, "test-username", "test-password");
		StubServiceInfo testServiceInfo2 = new StubServiceInfo("test-id", "test-host", 1000, "test-username", "test-password");		
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo1, testServiceInfo2);
		serviceConnectorCreators.add(new StubServiceConnectorCreator());
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		testCloud.getSingletonServiceConnector(null, null);
	}
	
	@Test
	public void serviceConnectorCreationSpecifiedTypeAndConfig() {
		StubServiceInfo testServiceInfo = new StubServiceInfo("test-id", "test-host", 1000, "test-username", "test-password");
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo);
		serviceConnectorCreators.add(new StubServiceConnectorCreator());
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		StubServiceConnectorConfig config = new StubServiceConnectorConfig("test-config");
		StubServiceConnector connector = testCloud.getServiceConnector(testServiceInfo.getId(), StubServiceConnector.class, config);
		
		assertStubService(testServiceInfo, connector, config);
	}
	
	@Test
	public void serviceInfoNoServices() {
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector();
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		assertEquals(0, testCloud.getServiceInfos().size());
		assertEquals(0, testCloud.getServiceInfos(StubServiceInfo.class).size());
	}
	
	@Test
	public void serviceInfoMultipleServicesOfTheSameType() {
		StubServiceInfo testServiceInfo1 = new StubServiceInfo("test-id1", "test-host", 1000, "test-username", "test-password");
		StubServiceInfo testServiceInfo2 = new StubServiceInfo("test-id2", "test-host", 1000, "test-username", "test-password");
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo1, testServiceInfo2);
		serviceConnectorCreators.add(new StubServiceConnectorCreator());
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);

		assertEquals(2, testCloud.getServiceInfos().size());
		assertEquals(2, testCloud.getServiceInfos(StubServiceConnector.class).size());
	}
	
	@Test
	public void getServiceInfoByValidId() {
		StubServiceInfo testServiceInfo1 = new StubServiceInfo("test-id1", "test-host", 1000, "test-username", "test-password");
		StubServiceInfo testServiceInfo2 = new StubServiceInfo("test-id2", "test-host", 1000, "test-username", "test-password");
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo1, testServiceInfo2);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);

		assertEquals(testServiceInfo1, testCloud.getServiceInfo(testServiceInfo1.getId()));
		assertEquals(testServiceInfo2, testCloud.getServiceInfo(testServiceInfo2.getId()));		
	}
	
	@Test
	public void servicePropsTwoServicesOfTheSameLabel() {
		StubServiceInfo testServiceInfo1 = new StubServiceInfo("test-id1", "test-host", 1000, "test-username", "test-password");
		StubServiceInfo testServiceInfo2 = new StubServiceInfo("test-id2", "test-host", 1000, "test-username", "test-password");
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo1, testServiceInfo2);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		Properties cloudProperties = testCloud.getCloudProperties();
		assertStubServiceProp("cloud.services.test-id1", testServiceInfo1, cloudProperties);
		assertStubServiceProp("cloud.services.test-id2", testServiceInfo2, cloudProperties);
		
		assertNull(cloudProperties.get("cloud.services.stub.connection.host"));
	}
	
	@Test
	public void servicePropsOneServiceOfTheSameLabel() {
		StubServiceInfo testServiceInfo = new StubServiceInfo("test-id", "test-host", 1000, "test-username", "test-password");

		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(testServiceInfo);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);
		
		Properties cloudProperties = testCloud.getCloudProperties();
		assertStubServiceProp("cloud.services.test-id", testServiceInfo, cloudProperties);
		assertStubServiceProp("cloud.services.stub", testServiceInfo, cloudProperties);
	}

	@Test(expected=CloudException.class)
	public void getServiceInfoByInvalidId() {
		StubCloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector();
		Cloud testCloud = new Cloud(stubCloudConnector, serviceConnectorCreators);

		testCloud.getServiceInfo("foo");
	}

	private void assertStubServiceProp(String leadKey, StubServiceInfo serviceInfo, Properties cloudProperties) {
		CloudTestUtil.assertBasicProps(leadKey, serviceInfo, cloudProperties);
		
		assertEquals(serviceInfo.getFoo(), cloudProperties.get(leadKey + ".bar"));
		assertNull(cloudProperties.get(leadKey + ".foo"));
	}
	
	private void assertStubService(StubServiceInfo serviceInfo, StubServiceConnector connector, StubServiceConnectorConfig config) {
		assertNotNull(connector);
		assertEquals(serviceInfo.getId(), connector.id);
		assertEquals(serviceInfo.getHost(), connector.host);
		assertEquals(serviceInfo.getPort(), connector.port);
		assertEquals(serviceInfo.getUserName(), connector.username);
		assertEquals(serviceInfo.getPassword(), connector.password);
		
		if (config == null) {
			assertNull(connector.config);
		} else {
			assertEquals(config.config, connector.config);
		}
	}
}

class StubServiceConnectorConfig implements ServiceConnectorConfig {
	public String config;
	
	public StubServiceConnectorConfig(String config) {
		this.config = config;
	}
}