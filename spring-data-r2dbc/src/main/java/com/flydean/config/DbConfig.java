package com.flydean.config;

import com.flydean.dao.UsersDao;
import com.flydean.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author wayne
 * @version DbConfig,  2020/9/13
 */
@Configuration
@Slf4j
public class DbConfig {

    @Bean
    public ApplicationRunner initDatabase(DatabaseClient client, UsersDao usersDao) {
        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS USERS;",
                "CREATE TABLE IF NOT EXISTS USERS ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);");

        statements.forEach(sql -> executeSql(client,sql)
                .doOnSuccess(count -> log.info("Schema created, rows updated: {}", count))
                .doOnError(error -> log.error("got error : {}",error.getMessage(),error))
                .subscribe()
        );

        return args ->getUser().flatMap(usersDao::save).subscribe(user -> log.info("User saved: {}", user));

    }

    private Flux<Users> getUser() {
        return Flux.just(new Users(null, "John", "Doe"), new Users(null, "Jane", "Doe"));
    }

    private Mono<Integer> executeSql(DatabaseClient client, String sql) {
        return client.execute(sql).fetch().rowsUpdated();
    }
}
