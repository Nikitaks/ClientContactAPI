package com.ClientContactAPI.swagger;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

  @Value("${openapi.dev-url}")
  private String devUrl;

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    Contact contact = new Contact();
    contact.setEmail("n.aksenov2015@yandex.ru");
    contact.setName("Nikita Aksenov");
    contact.setUrl("https://github.com/Nikitaks");

    License license = new License().name("No License informatin").url("#");

    Info info = new Info()
        .title("Client Contact Database API")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints to manage Client Contact Database.")
        .license(license);

    return new OpenAPI().addSecurityItem(new SecurityRequirement())
        .info(info).servers(Arrays.asList(devServer));
  }
}