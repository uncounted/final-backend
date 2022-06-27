package com.hanghae0705.sbmoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SbmoneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbmoneyApplication.class, args);
    }

}
