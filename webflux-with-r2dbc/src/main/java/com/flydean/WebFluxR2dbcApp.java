package com.flydean;

import com.flydean.config.DBConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author wayne
 * @version WebFluxR2dbcApp,  2020/9/13
 */
@SpringBootApplication
@EnableConfigurationProperties(DBConfig.class)
public class WebFluxR2dbcApp {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxR2dbcApp.class, args);
    }
}
