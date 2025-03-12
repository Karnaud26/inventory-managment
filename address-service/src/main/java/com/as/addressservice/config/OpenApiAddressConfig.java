package com.as.addressservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

@OpenAPIDefinition(
        servers = {
                @Server(url = "${api.server.dev.url}", description = "${api.server.dev.name}"),
                @Server(url = "${api.server.prod.url}", description = "${api.server.prod.name}")})
public class OpenApiAddressConfig {
    @Value("${api.server.dev.name}")
    private String devUrl;
    @Value("${api.server.prod.name}")
    private String prodUrl;
    /*@Value("${api.title}")
    private String title;
    @Value("${api.description}")
    private String description;
    @Value("${api.version}")
    private String version;
    @Value("${api.contact.name}")
    private String contactName;
    @Value("${api.contact.url}")
    private String contactUrl;
    @Value("${api.contact.email}")
    private String contactEmail;
    @Value("${api.tos.uri}")
    private String termsOfService;
    @Value("${api.licence.key}")
    private String licenceName;
    @Value("${api.licence.name}")
    private String licenceUrl;
    @Value("${api.licence.url}")*/

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Address API")
                        .version("v1.0")
                        .description("this.description")
                        .termsOfService("this.termsOfService")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("this.contactName")
                                .email("this.contactEmail")
                                .url("this.contactUrl")
                        )
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("this.licenceName")
                                .url("this.licenceUrl")
                        )
                );
    }
}
