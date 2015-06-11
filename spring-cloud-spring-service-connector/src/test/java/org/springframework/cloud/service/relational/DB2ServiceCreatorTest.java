package org.springframework.cloud.service.relational;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.DB2ServiceInfo;;

public class DB2ServiceCreatorTest extends AbstractDataSourceCreatorTest<DB2DataSourceCreator, DB2ServiceInfo> {
	@Mock private DB2ServiceInfo mockDB2ServiceInfo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// set a dummy JDBC driver since we can't yet include a real DB2 driver in the project due to licensing restrictions
		System.setProperty("spring-cloud.db2.driver", "com.example.Driver");
	}

	@Override
	public DB2ServiceInfo createServiceInfo() {
		when(mockDB2ServiceInfo.getJdbcUrl()).thenReturn("db2://10.20.30.40:50000/database-123:user=myuser;password=mypassword;");
		
		return mockDB2ServiceInfo;
	}

	@Override
	public String getDriverName() {
		return "com.example.Driver";
	}

	@Override
	public DB2DataSourceCreator getCreator() {
		return new DB2DataSourceCreator();
	}

	@Override
	public String getValidationQueryStart() {
		return "VALUES 1";
	}
}
