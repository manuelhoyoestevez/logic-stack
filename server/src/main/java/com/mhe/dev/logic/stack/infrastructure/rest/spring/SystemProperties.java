package com.mhe.dev.logic.stack.infrastructure.rest.spring;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * SystemProperties.
 */
public class SystemProperties
{
    private static Properties properties;

    /**
     * Get properties.
     *
     * @return Properties
     */
    public static Map<String, String> getProperties()
    {
        return properties == null ? null : properties
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    e -> e.getKey().toString(),
                    e -> e.getValue().toString()
                )
            );
    }

    public static void setProperties(Properties properties)
    {
        SystemProperties.properties = properties;
    }
}
