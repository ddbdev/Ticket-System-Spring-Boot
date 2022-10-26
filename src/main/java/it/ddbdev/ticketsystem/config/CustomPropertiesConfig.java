package it.ddbdev.ticketsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app")
//@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CustomPropertiesConfig {

    //private String tableNamePrefix;

}
