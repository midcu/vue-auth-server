package com.midcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
    public OpenAPI springShopOpenAPI() {

        return new OpenAPI()
			.info(
				new Info().title("SpringBoot API")
				.description("sample application")
				.version("v0.0.1")
			)
			.externalDocs(new ExternalDocumentation()
			.description("Springdoc Documentation")
			.url("https://springdoc.org/"))
			.addSecurityItem(new SecurityRequirement().addList("Authorization"))
			.components(new Components().addSecuritySchemes("Authorization",
          		new SecurityScheme().name("Authorization").description("jwt token").type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")));
    }
}
