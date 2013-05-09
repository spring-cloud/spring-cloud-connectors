package org.springframework.cloud.service;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class Util {
	public static boolean hasClass(String name) {
		try {
			Class.forName(name);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}
	
	public static void setCorrespondingProperties(BeanWrapper target, BeanWrapper source) {
		for (PropertyDescriptor pd : source.getPropertyDescriptors()) {
			String property = pd.getName();
			if (!"class".equals(property) && source.isReadableProperty(property) &&
						source.getPropertyValue(property) != null) {
				if (target.isWritableProperty(property)) {
					target.setPropertyValue(property, source.getPropertyValue(property));
				}
			}
		}
	}
}
