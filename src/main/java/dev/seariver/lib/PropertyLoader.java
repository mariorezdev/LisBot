package dev.seariver.lib;

import java.util.Properties;

public class PropertyLoader {

    public static void load() {

        try (var propFile = PropertyLoader.class.getClassLoader().getResourceAsStream("application.properties")) {

            var properties = new Properties();
            properties.load(propFile);

            var propertyNames = properties.stringPropertyNames();
            for (String key : propertyNames) {
                String envKey = key.replace('.', '_').toUpperCase();
                String propValue = System.getenv(envKey) != null ? System.getenv(envKey) : properties.getProperty(key);
                System.setProperty(key, propValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
