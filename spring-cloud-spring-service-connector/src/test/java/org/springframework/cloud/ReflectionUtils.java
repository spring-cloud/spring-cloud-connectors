package org.springframework.cloud;

import org.springframework.test.util.ReflectionTestUtils;

public class ReflectionUtils {
	@SuppressWarnings("EmptyCatchBlock")
	public static Object getValue(Object target, String... fieldNames) {
		for (String fieldName : fieldNames) {
			try {
				return ReflectionTestUtils.invokeGetterMethod(target, fieldName);
			} catch (IllegalArgumentException e1) {
				try {
					return ReflectionTestUtils.getField(target, fieldName);
				} catch (IllegalArgumentException e2) {
				}
			}
		}

		return null;
	}
}