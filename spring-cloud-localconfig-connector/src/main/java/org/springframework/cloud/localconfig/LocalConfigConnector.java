package org.springframework.cloud.localconfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.cloud.AbstractCloudConnector;
import org.springframework.cloud.FallbackServiceInfoCreator;
import org.springframework.cloud.KeyValuePair;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.app.BasicApplicationInstanceInfo;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.FallbackBaseServiceInfoCreator;

/**
 *
 * @author Christopher Smith
 *
 */
public class LocalConfigConnector extends AbstractCloudConnector<KeyValuePair> {

    private static final Logger logger = Logger.getLogger(LocalConfigConnector.class.getName());

    /*--------------- String constants for property keys ---------------*/

    public static final String PROPERTY_PREFIX = "spring.cloud.";

    public static final Pattern SERVICE_PROPERTY_PATTERN = Pattern.compile("\\A" + Pattern.quote(PROPERTY_PREFIX) + "(.+)" + "\\Z");

    public static final String APP_ID_PROPERTY = PROPERTY_PREFIX + "appId";

    public static final String PROPERTIES_FILE_PROPERTY = PROPERTY_PREFIX + "propertiesFile";

    /**
     * These properties configure the connector itself and aren't service definitions.
     */
    public static final List<String> META_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList(new String[] { APP_ID_PROPERTY, PROPERTIES_FILE_PROPERTY }));

    /*--------------- sources for service-definition properties ---------------*/

    static Properties programmaticProperties = new Properties();

    private Properties fileProperties = null;

    /*--------------- API implementation ---------------*/

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LocalConfigConnector() {
        super((Class) LocalConfigServiceInfoCreator.class);
    }

    /**
     * Returns {@code true} if a property named {@code spring.cloud.appId} is present in any of the property sources.
     * On the first call, attempts to load properties from a file specified in {@code spring.cloud.propertiesFile}.
     */
    @Override
    public boolean isInMatchingCloud() {
        if (fileProperties == null)
            readFileProperties();

        return findProperty(APP_ID_PROPERTY) != null;
    }

    @Override
    public ApplicationInstanceInfo getApplicationInstanceInfo() {
        return new BasicApplicationInstanceInfo(UUID.randomUUID().toString(), findProperty(APP_ID_PROPERTY),
            Collections.<String, Object> emptyMap());
    }

    @Override
    protected List<KeyValuePair> getServicesData() {
        if(fileProperties == null)
            throw new IllegalStateException("isInMatchingCloud() must be called first to initialize connector");

        LinkedHashMap<String, Properties> propertySources = new LinkedHashMap<String, Properties>();

        propertySources.put("programmatic properties", programmaticProperties);
        propertySources.put("properties from file", fileProperties);
        try {
            propertySources.put("system properties", System.getProperties());
        } catch (SecurityException e) {
            logger.log(Level.WARNING,
                "couldn't read system properties; no service definitions from system properties will be applied", e);
        }

        return LocalConfigUtil.readServicesData(propertySources);
    }

    @Override
    protected FallbackServiceInfoCreator<BaseServiceInfo, KeyValuePair> getFallbackServiceInfoCreator() {
        return new FallbackBaseServiceInfoCreator();
    }

    /*--------------- methods for manipulating properties and sources ---------------*/

    /**
     * Adds properties to be scanned from the supplied {@link InputStream}, overwriting
     * existing properties with the same name. Closes the stream after loading.
     *
     * @param propertiesInputStream
     *            a property list
     * @throws IOException
     *             if the underlying load operation throws an exception
     */
    public static void supplyProperties(final InputStream propertiesInputStream) throws IOException {
        programmaticProperties.load(propertiesInputStream);
        propertiesInputStream.close();
    }

    /**
     * Checks for the presence of a supplied or system property named {@code spring.cloud.propertiesFile}. If the property
     * is present, load its contents into {@link #fileProperties}. If there's a problem, log but continue.
     */
    private void readFileProperties() {
        fileProperties = new Properties();
        logger.fine("looking for a properties file");

        String filename = null;

        filename = programmaticProperties.getProperty(PROPERTIES_FILE_PROPERTY);

        try {
            filename = System.getProperty(PROPERTIES_FILE_PROPERTY, filename);
        } catch (SecurityException e) {
            logSystemReadException(PROPERTIES_FILE_PROPERTY, e);
            return;
        }

        if (filename == null) {
            logger.info("did not find a system property " + PROPERTIES_FILE_PROPERTY);
            return;
        }

        logger.info("loading properties from file " + filename);

        try {
            InputStream fis = openFile(filename);
            fileProperties.load(fis);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "exception while loading properties from file " + filename, e);
            return;
        }

        logger.info("properties loaded successfully");
    }

    /**
     * Broken out into a separate method for mocking the filesystem.
     * @param filename the file to open
     * @return a {@code FileInputStream} to the file
     * @throws IOException if opening the file throws
     */
    InputStream openFile(String filename) throws IOException {
        return new FileInputStream(filename);
    }

    /**
     * Look for a specific property in programmatically-supplied properties, properties from a file,
     * or the system properties. Last source wins.
     *
     * @param key
     *            the property to look for
     * @return the highest-priority value for the key, or {@code null} if the key is not found
     */
    private String findProperty(String key) {
        String value = programmaticProperties.getProperty(key);
        value = fileProperties.getProperty(key, value);
        try {
            value = System.getProperty(key, value);
        } catch (SecurityException e) {
            logSystemReadException(key, e);
        }

        return value;
    }

    private static void logSystemReadException(String key, SecurityException e) {
        logger.log(Level.WARNING, "couldn't read system property " + key, e);
    }
}
