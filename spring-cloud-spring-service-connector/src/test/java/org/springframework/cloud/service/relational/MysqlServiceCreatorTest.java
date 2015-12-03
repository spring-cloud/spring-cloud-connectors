package org.springframework.cloud.service.relational;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.MysqlServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class MysqlServiceCreatorTest extends AbstractDataSourceCreatorTest<MysqlDataSourceCreator, MysqlServiceInfo> {
	@Mock private MysqlServiceInfo mockMysqlServiceInfo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Override
	public MysqlServiceInfo createServiceInfo() {
		when(mockMysqlServiceInfo.getJdbcUrl()).thenReturn("jdbc:mysql://myuser:mypassword@10.20.30.40:3306/database-123");
		
		return mockMysqlServiceInfo;
	}

	@Override
	public MysqlDataSourceCreator getCreator() {
		return new MysqlDataSourceCreator();
	}

	@Override
	public String getValidationQueryStart() {
		return MysqlDataSourceCreator.VALIDATION_QUERY;
	}
}
