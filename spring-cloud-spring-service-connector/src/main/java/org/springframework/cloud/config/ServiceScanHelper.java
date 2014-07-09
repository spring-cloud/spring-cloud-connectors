package org.springframework.cloud.config;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.cloud.service.GenericCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceInfo;

/**
 * <p>
 * Each service populated by this bean has the same name as the service it is bound to.
 * </p>
 *
 * Usage:
 * Most applications should use either the Java config using {@link ServiceScan} annotation 
 * or XML config using &lt;cloud:service-scan/&gt; that introduce a bean of this type lgically 
 * equivalent to:
 * <pre>
 * &lt;bean class="org.cloudfoundry.runtime.service.CloudServicesScanner"/&gt;
 * </pre>
 * to have an easy access to all the services.
 *
 * If there is unique bean of a type, you can inject beans using the following
 * code (shows Redis, but the same scheme works for all services):
 * <pre>
 * &#64;Autowired RedisConnectionFactory redisConnectionFactory;
 * </pre>
 *
 * If there are more than one services of a type, you can use the @Qualifier
 * as in the following code:
 * <pre>
 * &#64;Autowired &#64;Qualifier("service-name1") RedisConnectionFactory redisConnectionFactory;
 * &#64;Autowired &#64;Qualifier("service-name2") RedisConnectionFactory redisConnectionFactory;
 * </pre>

 * @author Ramnivas Laddad
 *
 */
public class ServiceScanHelper {
    private static final String CLOUD_FACTORY_BEAN_NAME = "__cloud_factory__";
    private static Logger logger = Logger.getLogger(ServiceScanHelper.class.getName());
    
    private Cloud cloud;

    public void registerServiceBeans(BeanDefinitionRegistry registry) {
        initializeCloud(registry);
        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
        
        for(ServiceInfo serviceInfo: serviceInfos) {
            registerServiceBean(registry, serviceInfo);
        }
    }

    private void initializeCloud(BeanDefinitionRegistry registry) {
        if (cloud != null) {
            return;
        }
        
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
        
        if(beanFactory.getBeansOfType(CloudFactory.class).isEmpty()) {
            beanFactory.registerSingleton(CLOUD_FACTORY_BEAN_NAME, new CloudFactory());
        }
        CloudFactory cloudFactory = beanFactory.getBeansOfType(CloudFactory.class).values().iterator().next();
        cloud = cloudFactory.getCloud();
    }
    
    private void registerServiceBean(BeanDefinitionRegistry registry, ServiceInfo serviceInfo) {
        try {
            GenericCloudServiceConnectorFactory serviceFactory = 
                    new GenericCloudServiceConnectorFactory(serviceInfo.getId(), null);
            serviceFactory.setBeanFactory((BeanFactory) registry);
            serviceFactory.afterPropertiesSet();            
            BeanDefinitionBuilder definitionBuilder = 
                    BeanDefinitionBuilder.genericBeanDefinition(ScannedServiceWrapper.class);
            definitionBuilder.addConstructorArgValue(serviceFactory);
            definitionBuilder.getRawBeanDefinition().setAttribute(
                                      "factoryBeanObjectType", serviceFactory.getObjectType());
            registry.registerBeanDefinition(serviceInfo.getId(), definitionBuilder.getBeanDefinition());
        } catch (Exception ex) {
            logger.warning("Unable to create service for " + serviceInfo.getId() + " during service scanning. Skiping.");
        }
    }

    public static class ScannedServiceWrapper implements FactoryBean<Object> {
        private GenericCloudServiceConnectorFactory cloudServiceConnectorFactory;
        
        public ScannedServiceWrapper(GenericCloudServiceConnectorFactory cloudServiceConnectorFactory) {
            this.cloudServiceConnectorFactory = cloudServiceConnectorFactory;
        }
        
        @Override
        public Object getObject() throws Exception {
            return cloudServiceConnectorFactory.getObject();
        }

        @Override
        public Class<?> getObjectType() {
            return cloudServiceConnectorFactory.getObjectType();
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }


}
