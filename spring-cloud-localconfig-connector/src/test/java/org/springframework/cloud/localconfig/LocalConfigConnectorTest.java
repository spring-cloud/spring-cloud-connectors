package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class LocalConfigConnectorTest extends AbstractLocalConfigConnectorTest {

    public static final String APP_ID = "appId2";
    public static final String APP_ID_PROPERTY = LocalConfigConnector.APP_ID_PROPERTY + ": " + APP_ID;

    public static final String PROPERTY_FILE_NAME = "localconfig.nonsense.properties";
    public static final String PROPERTY_FILE_PROPERTY = LocalConfigConnector.PROPERTIES_FILE_PROPERTY + ": " + PROPERTY_FILE_NAME;

    @Test
    public void testNoAppIdAnywhere() {
        assertFalse(connector.isInMatchingCloud());
    }

    @Test
    public void testAppIdInConfigFile() throws IOException {
        env.setSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY, PROPERTY_FILE_NAME);

        connector.setFileProvider(StubbedOpenFileLocalConfigConnector.fileContentsFromString(PROPERTY_FILE_NAME, APP_ID_PROPERTY));

        assertTrue(connector.isInMatchingCloud());
        assertEquals(APP_ID, connector.getApplicationInstanceInfo().getAppId());
    }

    @Test
    public void testAppIdInFileAndSystem() throws IOException {
        env.setSystemProperty(LocalConfigConnector.PROPERTIES_FILE_PROPERTY, PROPERTY_FILE_NAME);
        env.setSystemProperty(LocalConfigConnector.APP_ID_PROPERTY, APP_ID);

        connector.setFileProvider(StubbedOpenFileLocalConfigConnector.fileContentsFromString(PROPERTY_FILE_NAME, APP_ID_PROPERTY));

        assertTrue(connector.isInMatchingCloud());
        assertEquals(APP_ID, connector.getApplicationInstanceInfo().getAppId());
    }
}
