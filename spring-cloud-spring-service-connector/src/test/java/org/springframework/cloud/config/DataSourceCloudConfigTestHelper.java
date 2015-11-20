package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.cloud.ReflectionUtils;

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
		assertEquals(connectionProp, ReflectionUtils.getValue(dataSource, "connectionProperties"));
	}

	public static void assertConnectionProperty(DataSource dataSource, String key, Object value) {
		assertEquals(value, ReflectionUtils.getValue(dataSource, key));
	}
}
