package it.ddbdev.ticketsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SetValuesFromApplicationYaml {

    public static String ISSUER;
    public static String JWT_SECRET;
    public static int JWT_EXPIRATION_IN_SECONDS;

    @Autowired
    public void getJwtProperties(
            @Value("${spring.application.name}") String issuer,
            @Value("${app.jwtSecret}") String jwtSecret,
            @Value("${app.jwtExpirationInSeconds}") int jwtExpirationInSeconds) {
        ISSUER = issuer;
        JWT_SECRET = jwtSecret;
        JWT_EXPIRATION_IN_SECONDS = jwtExpirationInSeconds;
    }
}