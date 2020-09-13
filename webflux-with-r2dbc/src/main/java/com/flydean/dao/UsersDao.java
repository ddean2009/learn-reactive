package com.flydean.dao;

import com.flydean.bean.Users;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wayne
 * @version UsersDao,  2020/9/13
 */
@Component
public class UsersDao {
    private ConnectionFactory connectionFactory;

    public UsersDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Mono<Users> findById(long id) {

        return Mono.from(connectionFactory.create())
                .flatMap(c -> Mono.from(c.createStatement("select id,firstname,lastname from Users where id = $1")
                        .bind("$1", id)
                        .execute())
                        .doFinally((st) -> close(c)))
                .map(result -> result.map((row, meta) ->
                        new Users(row.get("id", Long.class),
                                row.get("firstname", String.class),
                                row.get("lastname", String.class))))
                .flatMap( p -> Mono.from(p));
    }

    public Flux<Users> findAll() {

        return Mono.from(connectionFactory.create())
                .flatMap((c) -> Mono.from(c.createStatement("select id,firstname,lastname from users")
                        .execute())
                        .doFinally((st) -> close(c)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    Users acc = new Users();
                    acc.setId(row.get("id", Long.class));
                    acc.setFirstname(row.get("firstname", String.class));
                    acc.setLastname(row.get("lastname", String.class));
                    return acc;
                })));
    }

    public Mono<Users> createAccount(Users account) {

        return Mono.from(connectionFactory.create())
                .flatMap(c -> Mono.from(c.beginTransaction())
                        .then(Mono.from(c.createStatement("insert into Users(firstname,lastname) values($1,$2)")
                                .bind("$1", account.getFirstname())
                                .bind("$2", account.getLastname())
                                .returnGeneratedValues("id")
                                .execute()))
                        .map(result -> result.map((row, meta) ->
                                new Users(row.get("id", Long.class),
                                        account.getFirstname(),
                                        account.getLastname())))
                        .flatMap(pub -> Mono.from(pub))
                        .delayUntil(r -> c.commitTransaction())
                        .doFinally((st) -> c.close()));

    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close())
                .then(Mono.empty());
    }
}
