package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceCloudConfigTestHelper extends CommonPoolCloudConfigTestHelper {

	public static void assertPoolProperties(DataSource dataSource, int maxActive, int minIdle, long maxWait) {
	    assertCommonsPoolProperties(dataSource, maxActive, minIdle, maxWait);
	}

	public static void assertConnectionProperties(DataSource dataSource, Properties connectionProp) {
		assertEquals(connectionProp, ReflectionTestUtils.getField(dataSource, "connectionProperties"));
	}
}
