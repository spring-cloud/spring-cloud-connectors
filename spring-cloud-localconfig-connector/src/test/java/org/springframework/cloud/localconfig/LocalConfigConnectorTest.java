package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.service.UriBasedServiceData;

public class LocalConfigConnectorTest {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String APP_ID_1 = "appId1";
    public static final String APP_ID_1_PROPERTY = LocalConfigConnector.APP_ID_PROPERTY + ": " + APP_ID_1;

    public static final String APP_ID_2 = "appId2";
    public static final String APP_ID_2_PROPERTY = LocalConfigConnector.APP_ID_PROPERTY + ": " + APP_ID_2;

    public static final String PROPERTY_FILE_NAME = "localconfig.nonsense.properties";
    public static final String PROPERTY_FILE_PROPERTY = LocalConfigConnector.PROPERTIES_FILE_PROPERTY + ": " + PROPERTY_FILE_NAME;

    private static PassthroughEnvironmentAccessor bazEnv() {
        PassthroughEnvironmentAccessor bazEnv = new PassthroughEnvironmentAccessor();
        bazEnv.setSystemProperty("spring.cloud.baz", "inline!");
        return bazEnv;
    }

    public static class DetectAppIdTest {

        private LocalConfigConnector connector;

        private PassthroughEnvironmentAccessor env;

        @Before
        public void setup() {
            connector = new LocalConfigConnector();
            connector.setEnvironmentAccessor(env = bazEnv());
        }

        @After
        public void clearProperties() {
            LocalConfigConnector.programmaticProperties = new Properties();
        }

        @Test
        public void testNoAppIdAnywhere() {
            assertFalse(connector.isInMatchingCloud());
        }

        @Test
        public void testProgrammaticAppId() throws IOException {
            LocalConfigConnector.supplyProperties(new ByteArrayInputStream(APP_ID_1_PROPERTY.getBytes(UTF_8)));
            assertTrue(connector.isInMatchingCloud());
            assertEquals(APP_ID_1, connector.getApplicationInstanceInfo().getAppId());
        }

        @Test
        public void testProgrammaticAndFileAppIds() throws IOException {
            LocalConfigConnector.supplyProperties(new ByteArrayInputStream(APP_ID_1_PROPERTY.getBytes(UTF_8)));
            LocalConfigConnector.supplyProperties(new ByteArrayInputStream(PROPERTY_FILE_PROPERTY.getBytes(UTF_8)));

            LocalConfigConnector stubConnector = new LocalConfigConnector() {
                @Override
                InputStream openFile(String filename) throws IOException {
                    assertEquals(PROPERTY_FILE_NAME, filename);
                    return new ByteArrayInputStream(APP_ID_2_PROPERTY.getBytes(UTF_8));
                };
            };

            assertTrue(stubConnector.isInMatchingCloud());
            assertEquals(APP_ID_2, stubConnector.getApplicationInstanceInfo().getAppId());
        }

        @Test
        public void testProgrammaticFilenamePlusSystemAppId() throws IOException {
            LocalConfigConnector.supplyProperties(new ByteArrayInputStream(PROPERTY_FILE_PROPERTY.getBytes(UTF_8)));

            LocalConfigConnector stubConnector = new LocalConfigConnector() {
                @Override
                InputStream openFile(String filename) throws IOException {
                    assertEquals(PROPERTY_FILE_NAME, filename);
                    return new ByteArrayInputStream(APP_ID_2_PROPERTY.getBytes(UTF_8));
                };
            };

            env.setSystemProperty(LocalConfigConnector.APP_ID_PROPERTY, APP_ID_2);

            assertTrue(stubConnector.isInMatchingCloud());
            assertEquals(APP_ID_2, stubConnector.getApplicationInstanceInfo().getAppId());
        }
    }

    private LocalConfigConnector connector;

    InputStream propertiesFile;

    @Before
    public void setup() {
        connector = new LocalConfigConnector();
        propertiesFile = LocalConfigConnectorTest.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
        connector.setEnvironmentAccessor(bazEnv());
    }

    @After
    public void cleanup() throws IOException {
        LocalConfigConnector.programmaticProperties = new Properties();
        propertiesFile.close();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        LocalConfigConnector.supplyProperties(propertiesFile);

        assertTrue(connector.isInMatchingCloud());
        assertEquals("testApp", connector.getApplicationInstanceInfo().getAppId());

        List<UriBasedServiceData> services = connector.getServicesData();
        assertEquals(2, services.size());
        for (UriBasedServiceData service : services)
            if ("foo".equals(service.getKey()))
                assertEquals("bar", service.getUri());
    }

    @Test
    public void testLoadFromInputStreamWithOverride() throws IOException {
        LocalConfigConnector.supplyProperties(propertiesFile);

        assertTrue(connector.isInMatchingCloud());
        assertEquals("testApp", connector.getApplicationInstanceInfo().getAppId());

        List<UriBasedServiceData> services = connector.getServicesData();
        assertEquals(2, services.size());
        for(UriBasedServiceData service: services)
            if("baz".equals(service.getKey()))
                assertEquals("inline!", service.getUri());
    }
}
