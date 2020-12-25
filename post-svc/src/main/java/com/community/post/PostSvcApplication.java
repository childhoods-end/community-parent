package com.community.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.community.post.dao")
@SpringBootApplication
public class PostSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostSvcApplication.class, args);
	}

}
