package com.example.replyblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing        // 시간을 받아오는 어노테이션!! 까먹지 말자!!
@SpringBootApplication
public class ReplyBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReplyBlogApplication.class, args);
    }

}
