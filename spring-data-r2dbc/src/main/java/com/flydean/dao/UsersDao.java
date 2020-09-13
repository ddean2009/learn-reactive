package com.flydean.dao;


import com.flydean.entity.Users;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * @author wayne
 * @version UsersDao,  2020/9/13
 */
public interface UsersDao extends ReactiveCrudRepository<Users, Long> {

    @Query("select id, firstname, lastname from users c where c.lastname = :lastname")
    Flux<Users> findByLastname(String lastname);
}
