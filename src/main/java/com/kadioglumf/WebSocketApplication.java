package com.kadioglumf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class WebSocketApplication {
  public static void main(String[] args) {
    SpringApplication.run(WebSocketApplication.class, args);
  }
}
