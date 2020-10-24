package com.practice.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.practice")
@SpringBootApplication
public class MongodbApplication {
	public static void main(String[] args) {
		SpringApplication.run(MongodbApplication.class, args);
	}
}
