package org.springframework.cloud.service.relational;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlServiceCreatorWithDefaultDriverTest extends MysqlServiceCreatorTest {
	@Override
	public String getDriverName() {
		return MysqlDataSourceCreator.DRIVERS[0];
	}
}
