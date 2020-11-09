package com.practice.mongodb.repository.security;

import com.practice.mongodb.document.AuthenticatedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<AuthenticatedUser, String> {

}
