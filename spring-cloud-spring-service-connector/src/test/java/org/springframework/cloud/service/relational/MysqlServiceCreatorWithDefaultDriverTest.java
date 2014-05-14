package org.springframework.cloud.service.relational;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlServiceCreatorWithDefaultDriverTest extends MysqlServiceCreatorTest {
    private static final String MYSQL_DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
    
	@Override
	public String getDriverName() {
		return MYSQL_DRIVER_CLASS_NAME;
	}
}
