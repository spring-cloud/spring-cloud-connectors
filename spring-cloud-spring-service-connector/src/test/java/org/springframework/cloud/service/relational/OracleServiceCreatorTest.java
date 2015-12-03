package org.springframework.cloud.service.relational;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.OracleServiceInfo;

public class OracleServiceCreatorTest extends AbstractDataSourceCreatorTest<OracleDataSourceCreator, OracleServiceInfo> {
	public static final String TEST_ORACLE_DRIVER = "com.oracle.example.Driver";

	@Mock
	private OracleServiceInfo mockOracleServiceInfo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// set a dummy JDBC driver since we can't include a real Oracle driver in the project due to licensing restrictions
		System.setProperty("spring-cloud.oracle.driver", TEST_ORACLE_DRIVER);
	}

	@Override
	public OracleServiceInfo createServiceInfo() {
		when(mockOracleServiceInfo.getJdbcUrl()).thenReturn("oracle://myuser:mypassword@10.20.30.40:5432/database-123");
		
		return mockOracleServiceInfo;
	}

	@Override
	public String getDriverName() {
		return TEST_ORACLE_DRIVER;
	}

	@Override
	public OracleDataSourceCreator getCreator() {
		return new OracleDataSourceCreator();
	}

	@Override
	public String getValidationQueryStart() {
		return OracleDataSourceCreator.VALIDATION_QUERY;
	}
}
