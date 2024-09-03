package com.main.bitebyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.main.bitebyte")
@EnableMongoRepositories(basePackages = {
    "com.main.bitebyte.ai",
    "com.main.bitebyte.recipe",
    "com.main.bitebyte.ecommerce",
    "com.main.bitebyte.common",
    "com.main.bitebyte.livecooking",
    "com.main.bitebyte.user",
    "com.main.bitebyte.security"
})
public class BiteByteApplication {



    public static void main(String[] args) {
        SpringApplication.run(BiteByteApplication.class, args);
    }

}
