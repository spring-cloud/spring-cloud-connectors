package org.springframework.cloud;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.CompositeServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * Class to support testing.
 * 
 * <p>
 * NOTE: This class isn't meant to be consumed by end applications. However, it is added here (instead of in src/main/test)
 * to allow plugins to use this common testing infrastructure.
 * </p>
 *  
 * @author Ramnivas Laddad
 *
 */
public class CloudTestUtil {

	public static class StubCloudConnector implements CloudConnector {
		private List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
		private ApplicationInstanceInfo applicationInstance = new StubApplicationInstanceInfo("default-app-instance", "default-app", Collections.<String, Object> emptyMap());
	
		@Override
		public boolean isInMatchingCloud() {
			return true;
		}
	
		@Override
		public ApplicationInstanceInfo getApplicationInstanceInfo() {
			return applicationInstance;
		}
		
		@Override
		public List<ServiceInfo> getServiceInfos() {
			return serviceInfos ;
		}
		
		// Methods to set test environment
		public void addServiceInfo(ServiceInfo info) {
			serviceInfos.add(info);
		}
		
		public void setApplicationInstance(ApplicationInstanceInfo applicationInstance) {
			this.applicationInstance = applicationInstance;
		}
	}
	
	static public class StubApplicationInstanceInfo implements ApplicationInstanceInfo {
		private String instanceId;
		private String appId;
		private Map<String, Object> properties;

		public StubApplicationInstanceInfo(String instanceId, String appId, Map<String, Object> properties) {
			this.instanceId = instanceId;
			this.appId = appId;
			this.properties = properties;
		}
		
		@Override
		public String getInstanceId() {
			return instanceId;
		}

		@Override
		public String getAppId() {
			return appId;
		}

		@Override
		public Map<String, Object> getProperties() {
			return properties;
		}
	}
	
	@ServiceLabel("stub")
	public static class StubServiceInfo extends UriBasedServiceInfo {
	    public StubServiceInfo(String id, String host, int port, String username, String password) {
	        super(id, "stub", host, port, username, password, null);
	    }

	    // To test the scenario, where the name attribute of a property is explicitly specified
	    @ServiceProperty(name="bar")
	    public String getFoo() {
	        return "foo";
	    }
	}

	public static class TestServiceInfoTypeA extends BaseServiceInfo {
		public TestServiceInfoTypeA(String id) {
			super(id);
		}
	}

	public static class TestServiceInfoTypeB extends BaseServiceInfo {
		public TestServiceInfoTypeB(String id) {
			super(id);
		}
	}

	public static class StubCompositeServiceInfo implements CompositeServiceInfo {
        private String id;
	    private List<ServiceInfo> constituents;

        public StubCompositeServiceInfo(String id, ServiceInfo... constituents) {
            this.id = id;
	        this.constituents = Arrays.asList(constituents);
	    }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public List<ServiceInfo> getServiceInfos() {
            return constituents;
        }
	}
	
	public static StubCloudConnector getTestCloudConnector(ApplicationInstanceInfo applicationInstanceInfo, ServiceInfo... serviceInfos) {
		final StubCloudConnector stubCloudConnector = getTestCloudConnector(serviceInfos);
		stubCloudConnector.setApplicationInstance(applicationInstanceInfo);
		
		return stubCloudConnector;
	}

	public static StubCloudConnector getTestCloudConnector(ServiceInfo... serviceInfos) {
		final StubCloudConnector stubCloudConnector = new StubCloudConnector();
		for (ServiceInfo serviceInfo: serviceInfos) {
			stubCloudConnector.addServiceInfo(serviceInfo);
		}

		return stubCloudConnector;
	}
	

	public static void assertBasicProps(String leadKey, UriBasedServiceInfo serviceInfo, Properties cloudProperties) {
		assertEquals(serviceInfo.getId(), cloudProperties.get(leadKey + ".id"));
		assertEquals(serviceInfo.getHost(), cloudProperties.get(leadKey + ".connection.host"));
		assertEquals(serviceInfo.getPort(), cloudProperties.get(leadKey + ".connection.port"));
		assertEquals(serviceInfo.getUserName(), cloudProperties.get(leadKey + ".connection.username"));
		assertEquals(serviceInfo.getPassword(), cloudProperties.get(leadKey + ".connection.password"));
	}
}

