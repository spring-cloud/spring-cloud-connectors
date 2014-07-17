package org.springframework.cloud.localconfig;

import java.util.Properties;

import org.springframework.cloud.util.EnvironmentAccessor;

class PassthroughEnvironmentAccessor extends EnvironmentAccessor {
    private Properties systemProperties = new Properties(System.getProperties());

    void clear() {
        systemProperties.clear();
    }

    void setSystemProperty(String key, String value) {
        systemProperties.setProperty(key, value);
    }

    @Override
    public String getSystemProperty(String key, String defaultValue) {
        return systemProperties.getProperty(key, defaultValue);
    }

    @Override
    public Properties getSystemProperties() {
        return systemProperties;
    }
}
