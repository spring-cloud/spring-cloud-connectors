package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;

public class AbstractLocalConfigConnectorWithUrisTest extends AbstractLocalConfigConnectorTest {

	public static String PROPERTY_FILE_WITH_URIS = "localconfig.testuris.properties";

	@Before
	public void useTestUris() {
		InputStream testUrisProperties = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_WITH_URIS);
		env.setSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY, PROPERTY_FILE_WITH_URIS);
		connector.setFileProvider(StubbedOpenFileLocalConfigConnector.fileContentsFromStream(PROPERTY_FILE_WITH_URIS, testUrisProperties));
		assertTrue(connector.isInMatchingCloud());
	}
}
