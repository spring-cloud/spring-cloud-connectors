package org.springframework.cloud.localconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.springframework.cloud.AbstractCloudConnector.KeyValuePair;

public final class LocalConfigUtil {
    private static final Logger logger = Logger.getLogger(LocalConfigConnector.class.getName());

    private LocalConfigUtil() {
    }

    static List<KeyValuePair> readServicesData(LinkedHashMap<String, Properties> propertySources) {
        // we'll turn this into KVPs to return but need to eliminate duplicates first
        Map<String, String> collectedServices = new HashMap<String, String>();

        // iterate over the property sources in order, extracting matching properties
        for (Map.Entry<String, Properties> propertySource : propertySources.entrySet()) {
            if(propertySource.getValue().isEmpty()) {
                logger.info("no " + propertySource.getKey());
                continue;
            }
            logger.info("reading services from " + propertySource.getKey());
            Map<String, String> services = readServices(propertySource.getValue());

            // add each of the found services to the list, warning about duplicates
            for (Map.Entry<String, String> service : services.entrySet()) {
                String oldUri = collectedServices.put(service.getKey(), service.getValue());
                if (oldUri == null)
                    logger.info("added service '" + service.getKey() + "' from " + propertySource.getKey());
                else
                    logger.warning("replaced service '" + service.getKey() + "' with new URI from " + propertySource.getKey());
            }
        }

        // now we have a collated set of service IDs and URIs
        List<KeyValuePair> serviceData = new ArrayList<KeyValuePair>(collectedServices.size());
        for (Map.Entry<String, String> serviceInfo : collectedServices.entrySet()) {
            serviceData.add(new KeyValuePair(serviceInfo.getKey(), serviceInfo.getValue()));
        }

        return serviceData;
    }

    /**
     * Goes through a {@code Properties} object, finding all service definitions (properties
     * prefixed with {@code spring.cloud.} but not in {@code META_PROPERTIES}) and collects {@code (id,URI)} pairs.
     *
     * @param properties
     *            the {@code Properties} object to read
     * @return all of the service definitions found
     */
    static Map<String, String> readServices(Properties properties) {
        Map<String, String> services = new HashMap<String, String>();

        for (String propertyName : properties.stringPropertyNames()) {
            if (LocalConfigConnector.META_PROPERTIES.contains(propertyName)) {
                logger.finer("skipping meta property " + propertyName);
                continue;
            }

            Matcher m = LocalConfigConnector.SERVICE_PROPERTY_PATTERN.matcher(propertyName);
            if (!m.matches()) {
                logger.finest("skipping non-Spring-Cloud property " + propertyName);
                continue;
            }

            String serviceId = m.group(1);
            String serviceUri = properties.getProperty(propertyName);

            // no URI here because they will contain passwords
            logger.fine("found service URI for service " + serviceId);
            services.put(serviceId, serviceUri);
        }

        return services;
    }
}
