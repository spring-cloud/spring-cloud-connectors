package org.springframework.cloud.service.relational;

import java.util.ArrayList;
import java.util.List;

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
	public void pooledDataSourceCreation() {
		List<PooledDataSourceCreator<MysqlServiceInfo>> pooledDataSourceCreators = new ArrayList<PooledDataSourceCreator<MysqlServiceInfo>>();

		pooledDataSourceCreators.add(new BasicDbcpPooledDataSourceCreator<MysqlServiceInfo>());
		pooledDataSourceCreators.add(new TomcatDbcpPooledDataSourceCreator<MysqlServiceInfo>());
		pooledDataSourceCreators.add(new TomcatHighPerformancePooledDataSourceCreator<MysqlServiceInfo>());

		for (PooledDataSourceCreator<MysqlServiceInfo> testCreator : pooledDataSourceCreators) {
			DataSource ds = testCreator.create(mockMysqlServiceInfo, null, 
					                           mysqlDataSourceCreator.getDriverClassName(mockMysqlServiceInfo), 
					                           "select 1");
			
			Assert.assertNotNull(ds);
		}
	}
}
