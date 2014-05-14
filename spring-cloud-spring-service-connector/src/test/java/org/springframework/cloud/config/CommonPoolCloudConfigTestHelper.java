package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;

import org.springframework.test.util.ReflectionTestUtils;

public class CommonPoolCloudConfigTestHelper {
    
    public static void assertCommonsPoolProperties(Object pool, int maxActive, int minIdle, long maxWait) {
        assertEquals(maxActive, getValue(pool, "maxActive", "maxTotal"));
        assertEquals(minIdle, getValue(pool, "minIdle"));
        assertEquals(maxWait, getValue(pool, "maxWait", "maxWaitMillis"));        
    }
    
    protected static Object getValue(Object object, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                return ReflectionTestUtils.getField(object, fieldName);
            } catch (IllegalArgumentException ex) {
            }
        }
        return null;
    }

}
