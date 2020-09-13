package com.flydean.service;

import com.flydean.dao.UsersDao;
import com.flydean.entity.Users;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author wayne
 * @version UsersService,  2020/9/13
 */
@Component
public class UsersService {

    @Resource
    private UsersDao usersDao;

    @Transactional
    public Mono<Users> save(Users user) {

        return usersDao.save(user).map(it -> {

            if (it.getFirstname().equals("flydean")) {
                throw new IllegalStateException();
            } else {
                return it;
            }
        });
    }

}
