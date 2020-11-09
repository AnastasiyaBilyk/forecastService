package com.practice.mongodb.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.User;


import java.util.ArrayList;

@Document
@Data
public class AuthenticatedUser {

    @Id
    private String username;
    private String password;

    public AuthenticatedUser() { }

    public AuthenticatedUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toUserDetails() {
        return new User(username, password, new ArrayList<>());
    }
}
