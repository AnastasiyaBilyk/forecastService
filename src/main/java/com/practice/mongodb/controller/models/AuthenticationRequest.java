package com.practice.mongodb.controller.models;

import lombok.Getter;

@Getter
public class AuthenticationRequest {

    private String username;
    private String password;

    public AuthenticationRequest() { }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
