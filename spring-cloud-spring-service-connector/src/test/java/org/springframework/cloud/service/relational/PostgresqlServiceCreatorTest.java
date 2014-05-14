package org.springframework.cloud.service.relational;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 */
public class PostgresqlServiceCreatorTest extends AbstractDataSourceCreatorTest<PostgresqlDataSourceCreator, PostgresqlServiceInfo> {
	@Mock private PostgresqlServiceInfo mockPostgresqlServiceInfo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Override
	public PostgresqlServiceInfo createServiceInfo() {
		when(mockPostgresqlServiceInfo.getJdbcUrl()).thenReturn("jdbc:postgresql://myuser:mypassword@10.20.30.40:5432/database-123");
		
		return mockPostgresqlServiceInfo;
	}

	@Override
	public String getDriverName() {
		return "org.postgresql.Driver";
	}

	@Override
	public PostgresqlDataSourceCreator getCreator() {
		return new PostgresqlDataSourceCreator();
	}

	@Override
	public String getValidationQueryStart() {
		return "SELECT 1";
	}
}
