package com.flydean.controller;

import com.flydean.dao.UsersDao;
import com.flydean.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author wayne
 * @version UsersController,  2020/9/13
 */
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersDao usersDao;

    @GetMapping("/users")
    public Flux<Users> findAll() {
        return usersDao.findAll();
    }
}
