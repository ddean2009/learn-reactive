package com.flydean.controller;

import com.flydean.bean.Users;
import com.flydean.dao.UsersDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wayne
 * @version UsersController,  2020/9/13
 */
@RestController
public class UsersController {

    private final UsersDao usersDao;

    public UsersController(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<Users>> getUsers(@PathVariable("id") Long id) {

        return usersDao.findById(id)
                .map(acc -> new ResponseEntity<>(acc, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(null, HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/users")
    public Flux<Users> getAllAccounts() {
        return usersDao.findAll();
    }

    @PostMapping("/createUser")
    public Mono<ResponseEntity<Users>> createUser(@RequestBody Users user) {
        return usersDao.createAccount(user)
                .map(acc -> new ResponseEntity<>(acc, HttpStatus.CREATED))
                .log();
    }
}
