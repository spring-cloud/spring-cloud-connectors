package org.springframework.cloud;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class TestServiceInfoCreator implements ServiceInfoCreator<TestServiceInfo, TestServiceData> {

	@Override
	public boolean accept(TestServiceData serviceData) {
		return serviceData.getTag().equals("test-tag");
	}

	@Override
	public TestServiceInfo createServiceInfo(TestServiceData serviceData) {
		return new TestServiceInfo(serviceData.getId(), serviceData.getTag());
	}
	
}