package com.example.ticket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	
	 @Bean
	    public OpenAPI ticketManagementOpenAPI() {
	        return new OpenAPI()
	            .info(new Info()
	                .title("API de Gestion de Tickets")
	                .description("Système de gestion de tickets avec Spring Boot")
	                .version("1.0.0")
	                .contact(new Contact()
	                    .name("Équipe Développement")
	                    .email("dev@ticketsystem.com"))
	                .license(new License()
	                    .name("Apache 2.0")
	                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
	            .components(new Components()
	                .addSecuritySchemes("basicAuth", new SecurityScheme()
	                    .type(SecurityScheme.Type.HTTP)
	                    .scheme("basic")))
	            .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
	    }

}
