package org.springframework.cloud.service.relational;

import org.junit.After;
import org.junit.Before;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlServiceCreatorWithMysqlDriverTest extends MysqlServiceCreatorTest {
	private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	@Before
	public void setup() {
		super.setup();
		System.setProperty("spring-cloud.mysql.driver", MYSQL_DRIVER_CLASS_NAME);
	}
	
	@After
	public void tearDown() {
		System.setProperty("spring-cloud.mysql.driver", "");
	}

	@Override
	public String getDriverName() {
		return MYSQL_DRIVER_CLASS_NAME;
	}
}
