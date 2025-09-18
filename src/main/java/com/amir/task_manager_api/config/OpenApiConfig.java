package com.amir.task_manager_api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("Простой REST API для управления задачами. " +
                                "Поддерживает создание, получение, обновление и удаление задач.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Амир Сыргабаев")
                                .email("syrgabaew.amir@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub репозиторий проекта")
                        .url("https://github.com/kyljmeeski/task-manager-api"));
    }

}
