package org.springframework.cloud;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class TestServiceInfoCreator implements ServiceInfoCreator<TestServiceInfo> {

	@Override
	public boolean accept(Object serviceData) {
		TestServiceData serviceDataMap = (TestServiceData) serviceData;
		
		return serviceDataMap.getTag().equals("test-tag");
	}

	@Override
	public TestServiceInfo createServiceInfo(Object serviceData) {
		TestServiceData serviceDataMap = (TestServiceData) serviceData;
		
		return new TestServiceInfo(serviceDataMap.getId(), serviceDataMap.getTag());
	}
	
}