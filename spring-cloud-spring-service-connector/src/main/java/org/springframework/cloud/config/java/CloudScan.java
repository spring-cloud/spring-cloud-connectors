package org.springframework.cloud.config.java;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Add this annotation to an &#64;{@link Configuration} class to have a bean for each
 * service bound to the app as well as one for {@link ApplicationInstanceInfo} added to 
 * the application context.
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;CloudScan
 * public class CloudConfiguration {
 *     // may (optionally) extend AbstractCloudConfiguration
 * }
 * </pre>
 * 
 * This annotation is similar to &#064;ComponentScan in Spring, which scans for classes
 * with the &#064;Component classes and creates a bean for each. &#064;CloudScan, in the same
 * spirit, scans services bound to the app and creates a bean for each. It also creates a bean
 * of {@link ApplicationInstanceInfo} class to expose information about the application instance. 
 * 
 * Upon service scanning, if there is a unique bean for service type, you can inject it
 * using the following code (shows Redis, but the same scheme works for all services):
 * <pre>
 * &#64;Autowired RedisConnectionFactory redisConnectionFactory;
 * </pre>
 *
 * If there are more than one services of a type, you can use the &#064;Qualifier annotation
 * as in the following code:
 * <pre>
 * &#64;Autowired &#64;Qualifier("service-name1") RedisConnectionFactory redisConnectionFactory;
 * &#64;Autowired &#64;Qualifier("service-name2") RedisConnectionFactory redisConnectionFactory;
 * </pre>
 * 
 * Similarly, application can have {@link ApplicationInstanceInfo} injected as follows:
 * <pre> 
 * &#64;Autowired ApplicationInstanceInfo applicationInstanceInfo;
 * </pre>
 *  
 * Note the difference between &#64;{@link ServiceScan} and this annotation. While the former only
 * adds beans for each bound service, the &#64;{@link CloudScan} annotation also adds a
 * bean for {@link ApplicationInstanceInfo}.
 * 
 * @see ServiceScan 
 * @author Ramnivas Laddad
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CloudScanConfiguration.class)
public @interface CloudScan {

}
