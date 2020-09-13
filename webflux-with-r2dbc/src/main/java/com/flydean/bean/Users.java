package com.flydean.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayne
 * @version Users,  2020/9/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private Long id;
    private String firstname;
    private String lastname;
}
