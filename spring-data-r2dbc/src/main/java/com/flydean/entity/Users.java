package com.flydean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author wayne
 * @version Users,  2020/9/13
 */
@Data
@AllArgsConstructor
public class Users {

    @Id
    private Integer id;
    private String firstname;
    private String lastname;

    boolean hasId() {
        return id != null;
    }
}
