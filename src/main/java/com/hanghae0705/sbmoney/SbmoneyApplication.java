package com.hanghae0705.sbmoney;

import com.hanghae0705.sbmoney.security.oauth.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@EnableConfigurationProperties(AppProperties.class)
public class SbmoneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbmoneyApplication.class, args);
    }

}
