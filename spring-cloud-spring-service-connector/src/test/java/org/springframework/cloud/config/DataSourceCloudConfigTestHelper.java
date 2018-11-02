package org.springframework.cloud.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.cloud.ReflectionUtils;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceCloudConfigTestHelper extends CommonPoolCloudConfigTestHelper {

	public static void assertPoolProperties(DataSource dataSource, int maxActive, int minIdle, long maxWait) {
		if (dataSource instanceof DelegatingDataSource) {
			dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
		}
	    assertCommonsPoolProperties(dataSource, maxActive, minIdle, maxWait);

	}

	public static void assertConnectionProperties(DataSource dataSource, Properties connectionProp) {
		if (dataSource instanceof DelegatingDataSource) {
			dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
		}
		assertEquals(connectionProp, ReflectionUtils.getValue(dataSource, "connectionProperties"));
	}

	public static void assertConnectionProperty(DataSource dataSource, String key, Object value) {
		if (dataSource instanceof DelegatingDataSource) {
			dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
		}
		assertEquals(value, ReflectionUtils.getValue(dataSource, key));
	}

	public static void assertDataSourceType(DataSource dataSource, String className) throws ClassNotFoundException {
		assertDataSourceType(dataSource, Class.forName(className));
	}

	public static void assertDataSourceType(DataSource dataSource, Class clazz) throws ClassNotFoundException {
		if (dataSource instanceof DelegatingDataSource) {
			dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
		}
		assertThat(dataSource, instanceOf(clazz));
	}
}
