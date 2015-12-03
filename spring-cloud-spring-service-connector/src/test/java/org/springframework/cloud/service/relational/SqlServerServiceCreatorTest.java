package org.springframework.cloud.service.relational;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.SqlServerServiceInfo;

public class SqlServerServiceCreatorTest extends AbstractDataSourceCreatorTest<SqlServerDataSourceCreator, SqlServerServiceInfo> {
	public static final String TEST_SQLSERVER_DRIVER = "com.sqlserver.example.Driver";

	@Mock
	private SqlServerServiceInfo mockSqlServerServiceInfo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// set a dummy JDBC driver since we can't include a real SQL-Server driver in the project due to licensing restrictions
		System.setProperty("spring-cloud.sqlserver.driver", TEST_SQLSERVER_DRIVER);
	}

	@Override
	public SqlServerServiceInfo createServiceInfo() {
		when(mockSqlServerServiceInfo.getJdbcUrl()).thenReturn("sqlserver://myuser:mypassword@10.20.30.40:1433/database-123");
		
		return mockSqlServerServiceInfo;
	}

	@Override
	public String getDriverName() {
		return TEST_SQLSERVER_DRIVER;
	}

	@Override
	public SqlServerDataSourceCreator getCreator() {
		return new SqlServerDataSourceCreator();
	}

	@Override
	public String getValidationQueryStart() {
		return "SELECT 1";
	}
}
