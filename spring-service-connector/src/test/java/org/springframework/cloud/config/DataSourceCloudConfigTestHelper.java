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
public class DataSourceCloudConfigTestHelper {

	public static void assertPoolProperties(DataSource dataSource, int maxActive, int minIdle, long maxWait) {
		assertEquals(maxActive, ReflectionTestUtils.getField(dataSource, "maxActive"));
		assertEquals(minIdle, ReflectionTestUtils.getField(dataSource, "minIdle"));
		assertEquals(maxWait, ReflectionTestUtils.getField(dataSource, "maxWait"));		
	}

	public static void assertConnectionProperties(DataSource dataSource, Properties connectionProp) {
		assertEquals(connectionProp, ReflectionTestUtils.getField(dataSource, "connectionProperties"));
	}
}
