package com.practice.mongodb.service.security;

import com.practice.mongodb.document.AuthenticatedUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyUserDetailsService extends UserDetailsService {
     void saveUser(AuthenticatedUser user);
}
