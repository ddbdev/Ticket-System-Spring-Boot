package it.ddbdev.ticketsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Ticket System DDBDEV API", version = "3.0", description = "Ticket System"))
@SecurityScheme(
        name = "ddbdev_ticketsystem",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@EnableAsync
public class DDBTicketSystem {

    public static void main(String[] args) {
        SpringApplication.run(DDBTicketSystem.class, args);
    }

}
