package org.springframework.cloud.config.java;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class GenericServiceJavaConfigTest extends AbstractCloudJavaConfigTest<DataSource> {
	public GenericServiceJavaConfigTest() {
		super(GenericServiceWithId.class, GenericServiceWithoutId.class);
	}
	
	protected ServiceInfo createService(String id) {
		return createMysqlService(id);
	}
	
	protected Class<DataSource> getConnectorType() {
		return DataSource.class;
	}
	
	@Test
	public void cloudGenericServiceWithConnectorType() {
		ApplicationContext testContext = 
			getTestApplicationContext(GenericServiceWithConnectorType.class, createService("my-service"));		
		
		assertNotNull("Getting service with connector type (specific service)", 
					  testContext.getBean("myServiceWithTypeWithServiceName", DataSource.class));		

		assertNotNull("Getting service with connector type (unique service)", 
				      testContext.getBean("myServiceWithTypeWithoutServiceName", DataSource.class));		
	}
	
}

class GenericServiceWithId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public Object testService() {
		return service("my-service");
	}
	
}

class GenericServiceWithoutId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public Object testService() {
		return service();
	}
}

class GenericServiceWithConnectorType extends AbstractCloudConfig {
	@Bean
	public DataSource myServiceWithTypeWithServiceName() {
		return service("my-service", DataSource.class);
	}

	@Bean
	public DataSource myServiceWithTypeWithoutServiceName() {
		return service(DataSource.class);
	}
}
