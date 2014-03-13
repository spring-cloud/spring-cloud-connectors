package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.MysqlServiceInfo;

public class PooledDataSourceCreatorsTest {
	@Mock private MysqlServiceInfo mockMysqlServiceInfo;

	// Just to grab driver class name and validation query string 
	private MysqlDataSourceCreator mysqlDataSourceCreator = new MysqlDataSourceCreator();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void pooledDataSourceCreationDbcp() {
	    assertPooledDataSource(new BasicDbcpPooledDataSourceCreator<MysqlServiceInfo>());
	}

    @Test
    public void pooledDataSourceCreationTomcatDbcp() {
        assertPooledDataSource(new TomcatDbcpPooledDataSourceCreator<MysqlServiceInfo>());
    }

    @Test
    public void pooledDataSourceCreationTomcatHighPerformance() {
        assertPooledDataSource(new TomcatHighPerformancePooledDataSourceCreator<MysqlServiceInfo>());
    }
    
	private void assertPooledDataSource(PooledDataSourceCreator<MysqlServiceInfo> testCreator) {
        DataSource ds = testCreator.create(mockMysqlServiceInfo, null, 
                mysqlDataSourceCreator.getDriverClassName(mockMysqlServiceInfo), 
                "select 1");

        Assert.assertNotNull("Failed to create datasource with " + testCreator.getClass().getSimpleName(), ds);

	}
}
