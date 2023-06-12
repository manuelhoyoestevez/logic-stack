package com.mhe.dev.logic.stack.infrastructure.rest.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Spring Fox Config.
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig
{
    /**
     * API docket.
     *
     * @return Docket.
     */
    @Bean
    public Docket apiDocket()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors
                .basePackage("com.mhe.dev.logic.stack.infrastructure.rest.spring.controller"))
            .build();
    }
}
