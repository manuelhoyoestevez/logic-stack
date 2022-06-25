package com.mhe.dev.logic.stack.infrastructure.rest.spring;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertiesReader.
 */
public class PropertiesReader
{
    /**
     * Read properties from classpath file.
     *
     * @param propertyFileName classpath property file.
     * @return Properties loaded, or empty if error happens.
     */
    public static Properties readPropertiesFromClassPathIgnoringErrors(String propertyFileName)
    {
        Properties properties = new Properties();
        InputStream is = PropertiesReader.class.getClassLoader().getResourceAsStream(propertyFileName);

        if (is == null)
        {
            return properties;
        }

        try
        {
            properties.load(is);
        } catch (IOException ignored)
        {
            ;
        }

        return properties;
    }
}
