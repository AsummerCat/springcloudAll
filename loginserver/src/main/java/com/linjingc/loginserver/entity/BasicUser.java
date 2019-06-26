package com.linjingc.loginserver.entity;

import lombok.Data;

@Data
public class BasicUser {
    private long id;
    private String username;
    private String password;
}
