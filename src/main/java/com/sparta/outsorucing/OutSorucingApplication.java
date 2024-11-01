package com.sparta.outsorucing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OutSorucingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutSorucingApplication.class, args);
    }

}
