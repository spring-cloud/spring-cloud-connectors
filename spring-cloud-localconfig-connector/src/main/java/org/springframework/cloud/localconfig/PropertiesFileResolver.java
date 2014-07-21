package org.springframework.cloud.localconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.cloud.util.EnvironmentAccessor;

/**
 * Helper class that handles finding and merging properties to be read by the connector.
 *
 * @author Christopher Smith
 *
 */
class PropertiesFileResolver {

    public static final String BOOTSTRAP_PROPERTIES_FILENAME = "spring-cloud-bootstrap.properties";

    private static final Logger logger = Logger.getLogger(PropertiesFileResolver.class.getName());

    private final EnvironmentAccessor env;

    private final String classpathPropertiesFilename;

    PropertiesFileResolver(final EnvironmentAccessor env, final String classpathPropertiesFilename) {
        this.env = env;
        this.classpathPropertiesFilename = classpathPropertiesFilename;
    }

    PropertiesFileResolver(final EnvironmentAccessor env) {
        this(env, BOOTSTRAP_PROPERTIES_FILENAME);
    }

    PropertiesFileResolver() {
        this(new EnvironmentAccessor());
    }

    /**
     * Inspects the system properties for an entry named {@value LocalConfigConnector#PROPERTIES_FILE_PROPERTY} directing it to an
     * external properties file.
     *
     * @return a {@code File} pointing to the external properties file, or {@code null} if the system property couldn't be read
     */
    File findCloudPropertiesFileFromSystem() {
        String filename = null;

        try {
            filename = env.getSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY);
        } catch (SecurityException e) {
            logger.log(Level.WARNING, "SecurityManager prevented reading system property "
                + LocalConfigConnector.PROPERTIES_FILE_PROPERTY, e);
            return null;
        }

        if (filename == null) {
            logger.fine("didn't find system property for a configuration file");
            return null;
        }

        File file = new File(filename);
        logger.info("found system property for a configuration file: " + file);
        return file;
    }

    /**
     * Looks for a resource named {@code filename} (usually {@value #BOOTSTRAP_PROPERTIES_FILENAME}) on the classpath. If present,
     * it is loaded and
     * inspected for a property named {@value LocalConfigConnector#PROPERTIES_FILE_PROPERTY}, which is interpolated from the system
     * properties and returned.
     *
     * @return the filename derived from the classpath control file, or {@code null} if one couldn't be found
     */
    File findCloudPropertiesFileFromClasspath() {
        // see if we have a spring-cloud.properties at all
        InputStream in = getClass().getClassLoader().getResourceAsStream(classpathPropertiesFilename);
        if (in == null) {
            logger.info("no " + classpathPropertiesFilename
                + " found on the classpath to direct us to an external properties file");
            return null;
        }

        // load it as a properties file
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "found " + classpathPropertiesFilename
                + " on the classpath but couldn't load it as a properties file", e);
            return null;
        }

        // read the spring.cloud.propertiesFile property from it
        String template = properties.getProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY);
        if (template == null) {
            logger.log(Level.SEVERE, "found properties file " + classpathPropertiesFilename
                + " on the classpath, but it didn't contain a property named " + LocalConfigConnector.PROPERTIES_FILE_PROPERTY);
            return null;
        }

        // if there's anything else, the client probably tried to put an app ID or other credentials there
        if (properties.entrySet().size() > 1)
            logger.warning("the properties file " + classpathPropertiesFilename + " contained properties besides "
                + LocalConfigConnector.PROPERTIES_FILE_PROPERTY + "; ignoring");

        logger.fine("substituting system properties into '" + template + "'");
        File configFile = new File(new StrSubstitutor(systemPropertiesLookup(env)).replace(template));
        logger.info("derived configuration file name: " + configFile);

        return configFile;
    }

    File findCloudPropertiesFile() {
        File file = findCloudPropertiesFileFromSystem();

        if (file != null) {
            logger.info("using configuration file from system properties");
            return file;
        }

        file = findCloudPropertiesFileFromClasspath();

        if (file != null)
            logger.info("using configuration file derived from " + classpathPropertiesFilename);
        else
            logger.info("did not find any Spring Cloud configuration file");

        return file;
    }

    /**
     * Adapter from the {@link EnvironmentAccessor}'s system-property resolution to the {@code StrLookup} interface.
     *
     * @param env
     *            the {@code EnvironmentAccessor} to use for the lookups
     * @return a {@code StrLookup} view of the accessor's system properties
     */
    private StrLookup<String> systemPropertiesLookup(final EnvironmentAccessor env) {
        return new StrLookup<String>() {
            @Override
            public String lookup(String key) {
                return env.getSystemProperty(key);
            }
        };
    }
}
