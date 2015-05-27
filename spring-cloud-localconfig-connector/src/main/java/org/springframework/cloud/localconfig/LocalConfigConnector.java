package org.springframework.cloud.localconfig;

import java.io.File;
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
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.app.BasicApplicationInstanceInfo;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.FallbackBaseServiceInfoCreator;
import org.springframework.cloud.service.UriBasedServiceData;
import org.springframework.cloud.util.EnvironmentAccessor;

/**
 * @author Christopher Smith
 */
public class LocalConfigConnector extends AbstractCloudConnector<UriBasedServiceData> {

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
			Arrays.asList(APP_ID_PROPERTY, PROPERTIES_FILE_PROPERTY));

    /*--------------- inject system property access for testing ---------------*/

	private EnvironmentAccessor env = new EnvironmentAccessor();

	void setEnvironmentAccessor(EnvironmentAccessor env) {
		this.env = env;
	}

    /*--------------- API implementation ---------------*/

	@SuppressWarnings({"unchecked", "rawtypes"})
	public LocalConfigConnector() {
		super((Class) LocalConfigServiceInfoCreator.class);
	}

    /*--------------- properties read out of the file at spring.cloud.propertiesFile ---------------*/

	private Properties fileProperties = null;

	/**
	 * Returns {@code true} if a property named {@code spring.cloud.appId} is present in any of the property sources.
	 * On the first call, attempts to load properties from a file specified in {@code spring.cloud.propertiesFile}.
	 */
	@Override
	public boolean isInMatchingCloud() {
		if (fileProperties == null)
			readFileProperties();

		String appId = findProperty(APP_ID_PROPERTY);

		if (appId == null)
			logger.info("the property " + APP_ID_PROPERTY + " was not found in the system properties or configuration file");

		return appId != null;
	}

	@Override
	public ApplicationInstanceInfo getApplicationInstanceInfo() {
		return new BasicApplicationInstanceInfo(UUID.randomUUID().toString(), findProperty(APP_ID_PROPERTY),
				Collections.<String, Object>emptyMap());
	}

	@Override
	protected List<UriBasedServiceData> getServicesData() {
		if (fileProperties == null)
			throw new IllegalStateException("isInMatchingCloud() must be called first to initialize connector");

		LinkedHashMap<String, Properties> propertySources = new LinkedHashMap<String, Properties>();

		propertySources.put("properties from file", fileProperties);
		try {
			propertySources.put("system properties", env.getSystemProperties());
		} catch (SecurityException e) {
			logger.log(Level.WARNING,
					"couldn't read system properties; no service definitions from system properties will be applied", e);
		}

		return LocalConfigUtil.readServicesData(propertySources);
	}

	@Override
	protected FallbackServiceInfoCreator<BaseServiceInfo, UriBasedServiceData> getFallbackServiceInfoCreator() {
		return new FallbackBaseServiceInfoCreator();
	}

    /*--------------- methods for manipulating properties and sources ---------------*/

	/**
	 * Checks for the presence of a supplied or system property named {@code spring.cloud.propertiesFile}. If the property
	 * is present, load its contents into {@link #fileProperties}. If there's a problem, log but continue.
	 */
	private void readFileProperties() {
		fileProperties = new Properties();
		logger.fine("looking for a properties file");

		// will search system properties and the classpath
		File propertiesFile = new PropertiesFileResolver(env).findCloudPropertiesFile();

		if (propertiesFile == null) {
			logger.info("not loading service definitions from a properties file");
			return;
		}

		if (!fileExists(propertiesFile)) {
			logger.info("properties file " + propertiesFile + " does not exist; probably running in a real cloud");
			return;
		}

		logger.info("loading service definitions from properties file " + propertiesFile);

		try {
			InputStream fis = openFile(propertiesFile);
			fileProperties.load(fis);
			fis.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "exception while loading properties from file " + propertiesFile, e);
			return;
		}

		logger.info("properties loaded successfully");
	}

	/**
	 * Broken out into a separate method for mocking the filesystem.
	 *
	 * @param file the file to check
	 * @return whether the file exists
	 */
	boolean fileExists(File file) {
		return file.exists();
	}

	/**
	 * Broken out into a separate method for mocking the filesystem.
	 *
	 * @param file the file to open
	 * @return a {@code FileInputStream} to the file
	 * @throws IOException if opening the file throws
	 */
	InputStream openFile(File file) throws IOException {
		return new FileInputStream(file);
	}

	/**
	 * Look for a specific property in the config file or the system properties.
	 *
	 * @param key the property to look for
	 * @return the preferred value for the key, or {@code null} if the key is not found
	 */
	private String findProperty(String key) {
		String value = fileProperties.getProperty(key);

		try {
			value = env.getSystemProperty(key, value);
		} catch (SecurityException e) {
			logSystemReadException(key, e);
		}

		return value;
	}

	private static void logSystemReadException(String key, SecurityException e) {
		logger.log(Level.WARNING, "couldn't read system property " + key, e);
	}
}
