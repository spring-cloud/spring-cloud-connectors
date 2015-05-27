package org.springframework.cloud.localconfig;

import static java.io.File.separator;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class PropertiesFileResolverTest {

	private PassthroughEnvironmentAccessor env;

	private PropertiesFileResolver resolver;

	private String PROPERTIES_FILE_NAME = separator + "foo" + separator + "bar.properties";

	@Before
	public void setDefaults() {
		env = new PassthroughEnvironmentAccessor();
		resolver = new PropertiesFileResolver(env);
	}

	@Test
	public void testSecurityExceptionHandling() {
		env = mock(PassthroughEnvironmentAccessor.class);
		resolver = new PropertiesFileResolver(env);

		when(env.getSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY)).thenThrow(new SecurityException());
		assertNull(resolver.findCloudPropertiesFileFromSystem());
		verify(env).getSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY);
	}

	@Test
	public void testMissingSystemProperty() {
		assertNull(resolver.findCloudPropertiesFileFromSystem());
	}

	@Test
	public void testSystemProperty() {
		env.setSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY, PROPERTIES_FILE_NAME);
		assertEquals(PROPERTIES_FILE_NAME, resolver.findCloudPropertiesFileFromSystem().getPath());
	}

	@Test
	public void testNoClasspathFile() {
		resolver = new PropertiesFileResolver(env, "bazquux.properties");
		assertNull(resolver.findCloudPropertiesFileFromClasspath());
	}

	@Test
	public void testClasspathFileWithoutKey() {
		resolver = new PropertiesFileResolver(env, "localconfig.testuris.properties");
		assertNull(resolver.findCloudPropertiesFileFromClasspath());
	}

	@Test
	public void testLiteral() {
		resolver = new PropertiesFileResolver(env, "spring-cloud-literal.properties");
		assertEquals(PROPERTIES_FILE_NAME,
				resolver.findCloudPropertiesFileFromClasspath().getPath());
	}

	@Test
	public void testTemplate() {
		resolver = new PropertiesFileResolver(env, "spring-cloud-template.properties");
		env.setSystemProperty("user.home", "/foo");
		assertEquals(PROPERTIES_FILE_NAME,
				resolver.findCloudPropertiesFileFromClasspath().getPath());
	}

	@Test
	public void testFromSystem() {
		env.setSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY, PROPERTIES_FILE_NAME);
		assertEquals(PROPERTIES_FILE_NAME, resolver.findCloudPropertiesFile().getPath());
	}

	@Test
	public void testFromClasspath() {
		resolver = new PropertiesFileResolver(env, "spring-cloud-template.properties");
		env.setSystemProperty("user.home", "/foo");
		assertEquals(PROPERTIES_FILE_NAME,
				resolver.findCloudPropertiesFile().getPath());
	}

	@Test
	public void testNowhere() {
		assertNull(resolver.findCloudPropertiesFile());
	}

	@Test
	public void testPrecedence() {
		env.setSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY, PROPERTIES_FILE_NAME);
		resolver = new PropertiesFileResolver(env, "spring-cloud-literal.properties");
		assertEquals(PROPERTIES_FILE_NAME, resolver.findCloudPropertiesFile().getPath());
	}
}
