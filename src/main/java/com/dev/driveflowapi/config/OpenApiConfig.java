package com.dev.driveflowapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI driveFlowOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("DriveFlow API")
                                .version("1.0.0")
                                .description(
                                        "REST API for vehicle and dealer management, including automatic address lookup by zip code."
                                )
                );
    }
}