package com.mhe.dev.logic.stack.infrastructure.rest.spring;

import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Spring Application Loader.
 */
@SpringBootApplication(scanBasePackages = "com.mhe.dev.logic.stack.infrastructure.rest.spring")
@EntityScan(basePackages = "com.mhe.dev.logic.stack.infrastructure.entity")
public class Application
{
    /**
     * Main program.
     *
     * @param args Program arguments
     */
    public static void main(String[] args)
    {
        Properties pomProperties = PropertiesReader.readPropertiesFromClassPathIgnoringErrors("pom.properties");
        SystemProperties.setProperties(pomProperties);
        SpringApplication.run(Application.class, args);
    }
}
