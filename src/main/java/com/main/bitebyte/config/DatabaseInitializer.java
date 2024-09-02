package com.main.bitebyte.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        dropVectorStoreCollection();
    }

    private void dropVectorStoreCollection() {
        try {
            String collectionName = "vector_store"; // Replace with the actual name of your Vector store collection
            if (mongoTemplate.collectionExists(collectionName)) {
                mongoTemplate.dropCollection(collectionName);
                logger.info("Vector store collection dropped successfully");
            } else {
                logger.info("Vector store collection does not exist, no action taken");
            }
        } catch (Exception e) {
            logger.error("Error dropping Vector store collection", e);
        }
    }
}