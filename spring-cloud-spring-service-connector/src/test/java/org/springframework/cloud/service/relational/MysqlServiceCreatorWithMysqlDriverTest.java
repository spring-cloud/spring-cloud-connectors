package org.springframework.cloud.service.relational;

import org.junit.After;
import org.junit.Before;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlServiceCreatorWithMysqlDriverTest extends MysqlServiceCreatorTest {
	private static final String TEST_MYSQL_DRIVER = "com.mysql.example.Driver";

	@Before
	public void setup() {
		super.setup();
		System.setProperty("spring-cloud.mysql.driver", TEST_MYSQL_DRIVER);
	}
	
	@After
	public void tearDown() {
		System.setProperty("spring-cloud.mysql.driver", "");
	}

	@Override
	public String getDriverName() {
		return TEST_MYSQL_DRIVER;
	}
}
