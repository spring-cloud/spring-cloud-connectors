package org.springframework.cloud.config.java;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericServiceJavaConfigTest extends AbstractServiceJavaConfigTest<DataSource> {
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
		return connectionFactory().service("my-service");
	}
	
}

class GenericServiceWithoutId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public Object testService() {
		return connectionFactory().service();
	}
}

class GenericServiceWithConnectorType extends AbstractCloudConfig {
	@Bean
	public DataSource myServiceWithTypeWithServiceName() {
		return connectionFactory().service("my-service", DataSource.class);
	}

	@Bean
	public DataSource myServiceWithTypeWithoutServiceName() {
		return connectionFactory().service(DataSource.class);
	}
}
