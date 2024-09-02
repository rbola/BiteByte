package com.main.bitebyte.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.main.bitebyte.model.LiveCookingClass;

public interface LiveCookingClassRepository extends MongoRepository<LiveCookingClass, Long> {
    // Add custom query methods if needed
}