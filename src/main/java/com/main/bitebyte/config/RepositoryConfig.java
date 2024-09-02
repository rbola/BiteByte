package com.main.bitebyte.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.main.bitebyte.repository")
public class RepositoryConfig {
    // Additional configuration if needed
}
