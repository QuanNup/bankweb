package com.example.bankapi.Config.SwaggerConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${openapi.title}") String title,
                           @Value("${openapi.version}") String version,
                           @Value("${openapi.description}") String description,
                           @Value("${openapi.serverURL}") String serverUrl,
                           @Value("${openapi.serverName}") String serverName) {
        return new OpenAPI().info(
                        new Info()
                                .description(description)
                                .title(title)
                                .version(version)
                                .license(new License().name("API License").url("http://domain.vn/license"))
                )
                .servers(List.of(new Server().url(serverUrl).description(serverName)))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )

                )
                .security(List.of(new SecurityRequirement().addList("bearerAuth"))
                );
    }

    @Bean
    public GroupedOpenApi noneAuthGroup() {
        return GroupedOpenApi.builder()
                .group("non-security-service")
                .packagesToScan("com.example.bankapi.Controller.Authentication")
                .build();
    }

    @Bean
    public GroupedOpenApi userGroup() {
        return GroupedOpenApi.builder()
                .group("user-service")
                .packagesToScan("com.example.bankapi.Controller.BankAccount", "com.example.bankapi.Controller.ReceiptController", "com.example.bankapi.Controller.UserController")

                .build();
    }

    @Bean
    public GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder()
                .group("admin-service")
                .packagesToScan("com.example.bankapi.Controller.AdminController")
                .build();
    }
}
