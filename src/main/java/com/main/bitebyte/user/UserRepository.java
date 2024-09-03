package com.main.bitebyte.user;

import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.main.bitebyte.util.SpringContextUtil;

import jakarta.annotation.PostConstruct;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @PostConstruct
    default void ensureCollectionExists() {
        MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }
    }
}