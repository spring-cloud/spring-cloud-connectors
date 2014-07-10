package org.springframework.cloud.localconfig;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.AbstractCloudConnector.KeyValuePair;

public class LocalConfigUtilTest {

    private Properties first, second;

    private LinkedHashMap<String, Properties> propertySources;

    @Before
    public void initProperties(){
        first = new Properties();
        second = new Properties();

        propertySources = new LinkedHashMap<String, Properties>();
        propertySources.put("first", first);
        propertySources.put("second", second);
    }

    @Test
    public void testPropertyParsing() {
        first.setProperty("spring.cloud.appId", "should skip me because I'm meta");
        first.setProperty("spring.cloud.service1", "one");
        first.setProperty("spring.cloud.", "should skip me because I don't have an ID");
        first.setProperty("spring.cloud.service.two", "two");
        first.setProperty("foobar", "should skip me because I don't match the prefix");

        Map<String, String> services = LocalConfigUtil.readServices(first);
        assertEquals(2, services.size());
        assertEquals("one", services.get("service1"));
        assertEquals("two", services.get("service.two"));
    }

    @Test
    public void testCollation() {
        first.setProperty("spring.cloud.first", "firstUri");
        second.setProperty("spring.cloud.second", "secondUri");

        List<KeyValuePair> serviceData = LocalConfigUtil.readServicesData(propertySources);
        assertEquals(2, serviceData.size());
        boolean foundFirst = false;

        for(KeyValuePair kvp : serviceData) {
            if(kvp.getKey().equals("first")) {
                assertEquals("firstUri", kvp.getValue());
                foundFirst = true;
            }
        }

        assertTrue(foundFirst);
    }

    @Test
    public void testOverride() {
        first.setProperty("spring.cloud.duplicate", "firstUri");
        second.setProperty("spring.cloud.duplicate", "secondUri");

        List<KeyValuePair> serviceData = LocalConfigUtil.readServicesData(propertySources);
        assertEquals(1, serviceData.size());
        KeyValuePair kvp = serviceData.get(0);
        assertEquals("duplicate", kvp.getKey());
        assertEquals("secondUri", kvp.getValue());
    }
}
