package org.springframework.cloud.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.Cloud;

/**
 * Service information.
 * 
 * @author Ramnivas Laddad
 *
 */
public interface ServiceInfo {
	public String getId();
	
	/**
	 * Annotation to mark service label (used in presenting properties) 
	 * @see Cloud#getCloudProperties()
	 */
	@Inherited
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ServiceLabel {
		String value(); // the label
	}
	
	/**
	 * Annotation to mark properties to expose as cloud properties 
	 * @see Cloud#getCloudProperties()
	 */
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ServiceProperty {
		String category() default "";
		String name() default "";
	}
}
