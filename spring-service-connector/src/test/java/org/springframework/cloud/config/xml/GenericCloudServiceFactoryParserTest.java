package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericCloudServiceFactoryParserTest extends AbstractCloudServiceConnectorFactoryParserTest<DataSource> {
	
	protected ServiceInfo createService(String id) {
		return createMysqlService(id);
	}
	
	@Test
	public void cloudGenericServiceWithConnectorType() {
		ApplicationContext testContext = 
			getTestApplicationContext("cloud-generic-with-connector-type.xml", createService("my-service"));		
		
		assertNotNull("Getting service with connector type (specific service)", 
					  testContext.getBean("my-service-with-type-with-service-name", DataSource.class));		

		assertNotNull("Getting service with connector type (unique service)", 
				      testContext.getBean("my-service-with-type-without-service-name", DataSource.class));		
	}
	

	@Override
	protected String getWithServiceIdContextFileName() {
		return "cloud-generic-with-service-id.xml";
	}

	@Override
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-generic-without-service-id.xml";
	}

	@Override
	protected Class<DataSource> getConnectorType() {
		return DataSource.class;
	}
}
