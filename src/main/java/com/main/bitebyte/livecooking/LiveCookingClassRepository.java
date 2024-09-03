package com.main.bitebyte.livecooking;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.main.bitebyte.livecooking.LiveCookingClass;

public interface LiveCookingClassRepository extends MongoRepository<LiveCookingClass, Long> {
    // Add custom query methods if needed
}