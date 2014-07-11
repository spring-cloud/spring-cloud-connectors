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
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ClearSystemProperties;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.springframework.cloud.AbstractCloudConnector.KeyValuePair;

public class LocalConfigConnectorTest {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String APP_ID_1 = "appId1";
    public static final String APP_ID_1_PROPERTY = LocalConfigConnector.APP_ID_PROPERTY + ": " + APP_ID_1;

    public static final String APP_ID_2 = "appId2";
    public static final String APP_ID_2_PROPERTY = LocalConfigConnector.APP_ID_PROPERTY + ": " + APP_ID_2;

    public static final String PROPERTY_FILE_NAME = "propFile";
    public static final String PROPERTY_FILE_PROPERTY = LocalConfigConnector.PROPERTIES_FILE_PROPERTY + ": " + PROPERTY_FILE_NAME;

    public static class DetectAppIdTest {

        private LocalConfigConnector connector;

        @Before
        public void setup() {
            connector = new LocalConfigConnector();
        }

        @After
        public void clearProperties() {
            LocalConfigConnector.programmaticProperties = new Properties();
        }

        @Rule
        public final ClearSystemProperties NO_APP_ID_PROPERTY = new ClearSystemProperties(LocalConfigConnector.APP_ID_PROPERTY);

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

            System.setProperty(LocalConfigConnector.APP_ID_PROPERTY, "helloApp");
            assertTrue(stubConnector.isInMatchingCloud());
            assertEquals("helloApp", stubConnector.getApplicationInstanceInfo().getAppId());
        }
    }

    private LocalConfigConnector connector;

    InputStream propertiesFile;

    @Before
    public void setup() {
        connector = new LocalConfigConnector();
        propertiesFile = LocalConfigConnectorTest.class.getClassLoader().getResourceAsStream("localconfig.properties");
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

        List<KeyValuePair> services = connector.getServicesData();
        assertEquals(2, services.size());
        for (KeyValuePair service : services)
            if ("foo".equals(service.getKey()))
                assertEquals("bar", service.getValue());
    }

    @Rule
    public ProvideSystemProperty BAZ_PROPERTY = new ProvideSystemProperty("spring.cloud.baz", "inline!");

    @Test
    public void testLoadFromInputStreamWithOverride() throws IOException {
        LocalConfigConnector.supplyProperties(propertiesFile);

        assertTrue(connector.isInMatchingCloud());
        assertEquals("testApp", connector.getApplicationInstanceInfo().getAppId());

        List<KeyValuePair> services = connector.getServicesData();
        assertEquals(2, services.size());
        for(KeyValuePair service: services)
            if("baz".equals(service.getKey()))
                assertEquals("inline!", service.getValue());
    }
}
