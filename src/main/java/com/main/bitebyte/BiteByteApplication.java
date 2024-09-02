package com.main.bitebyte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.main.bitebyte")
public class BiteByteApplication {


    private static final Logger logger = LoggerFactory.getLogger(BiteByteApplication.class);

    public static void main(String[] args) {
        logger.debug("Starting application...");
        SpringApplication.run(BiteByteApplication.class, args);
        logger.debug("Application started successfully.");
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
         //   System.out.println("OpenAI API Key: " + openaiApiKey);
        };
    }
}
