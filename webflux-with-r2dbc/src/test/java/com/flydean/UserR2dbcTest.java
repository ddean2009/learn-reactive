package com.flydean;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import com.flydean.bean.Users;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;

/**
 * @author wayne
 * @version UserR2dbcTest,  2020/9/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserR2dbcTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ConnectionFactory cf;

    @Before
    public void initDatabase() {

        Flux.from(cf.create())
                .flatMap(c ->
                        c.createBatch()
                                .add("drop table if exists Users")
                                .add("create table Users(id IDENTITY(1,1), firstname varchar(80) not null, lastname varchar(80) not null)")
                                .add("insert into Users(firstname,lastname) values ( 'jacken', 'ma') ")
                                .add("insert into Users(firstname,lastname) values ( 'superman', 'ya') ")
                                .execute()
                )
                .log()
                .blockLast();
    }

    @Test
    public void testGetUser() {

        webTestClient
                .get()
                .uri("/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Users.class)
                .value((user) -> {
                    assertThat(user.getId(),is(1l));
                });
    }

    @Test
    public void testAllUsers() {

        webTestClient
                .get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .value((users) -> {
                    assertThat(users.size(),not(is(0)));
                });
    }


    @Test
    public void testCreateUser() {

        webTestClient
                .post()
                .uri("/createUser")
                .syncBody(new Users(null,"bluce", "wayne" ))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Users.class)
                .value((user) -> {
                    assertThat(user.getId(),is(notNullValue()));
                });

    }
}
