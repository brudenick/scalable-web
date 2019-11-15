package com.waes.assignment.scalableweb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class with common beans definitions and properties values.
 */
@Configuration
public class ScalableWebConfig {

    /**
     * Title for the REST API documentation.
     */
    @Value("${scalable.web.api.title:Title not defined}")
    private String apiTitle;

    /**
     * Description for the REST API documentation.
     */
    @Value("${scalable.web.api.description:Description not defined}")
    private String apiDescription;

    /**
     * Version for the REST API documentation.
     */
    @Value("${scalable.web.api.version:Version not defined}")
    private String apiVersion;

    /**
     * Gets the Swagger3 OpenAPI singleton bean.
     *
     * @return the Swagger3 OpenAPI bean.
     */
    @Bean
    public OpenAPI getConfiguredDocumentation() {
        return new OpenAPI().info(new Info().title(apiTitle).version(apiVersion).description(apiDescription));
    }

}
