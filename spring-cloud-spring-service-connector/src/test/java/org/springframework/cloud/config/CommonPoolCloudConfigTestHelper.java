package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.springframework.cloud.ReflectionUtils.getValue;

public class CommonPoolCloudConfigTestHelper {

	public static void assertCommonsPoolProperties(Object pool, int maxActive, int minIdle, long maxWait) {
		assertEquals(maxActive, getValue(pool, "maxActive", "maxTotal"));
		assertEquals(minIdle, getValue(pool, "minIdle"));
		assertEquals(maxWait, (long) Long.valueOf(getValue(pool, "maxWait", "maxWaitMillis").toString()));
	}
}
