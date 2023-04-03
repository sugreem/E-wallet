package com.example.majorproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
public interface UserRepository extends JpaRepository<User, Integer> {

    // Query is not needed
    User findByUserId(String userId);

}
