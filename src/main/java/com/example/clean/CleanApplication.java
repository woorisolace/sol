package com.example.clean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CleanApplication {

  public static void main(String[] args) {
    SpringApplication.run(CleanApplication.class, args);
  }

}
