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
public class MysqlServiceCreatorTest extends AbstractDataSourceCreatorTest<MysqlDataSourceCreator, MysqlServiceInfo> {
	@Mock private MysqlServiceInfo mockMysqlServiceInfo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Override
	public MysqlServiceInfo createServiceInfo() {
		when(mockMysqlServiceInfo.getUrl()).thenReturn("jdbc:mysql://10.20.30.40:3306/database-123");
		when(mockMysqlServiceInfo.getUserName()).thenReturn("myuser");
		when(mockMysqlServiceInfo.getPassword()).thenReturn("mypass");
		
		return mockMysqlServiceInfo;
	}

	@Override
	public String getDriverName() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public MysqlDataSourceCreator getCreator() {
		return new MysqlDataSourceCreator();
	}

	@Override
	public String getValidationQueryStart() {
		return "/* ping */ SELECT 1";
	}
}
