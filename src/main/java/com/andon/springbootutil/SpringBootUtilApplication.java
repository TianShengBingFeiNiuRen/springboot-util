package com.andon.springbootutil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@ServletComponentScan
@SpringBootApplication
public class SpringBootUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootUtilApplication.class, args);
    }

}
