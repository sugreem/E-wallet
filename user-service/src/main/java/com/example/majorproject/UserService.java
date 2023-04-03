package com.example.majorproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private static final Integer AMOUNT = 10;
    private static final String USER_CREATE_TOPIC = "user_create";

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCacheRepository userCacheRepository;

    @Autowired
    ObjectMapper objectMapper;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;  //For spring security purpose

    public User getUser(String userId){
        User user = userCacheRepository.getUser(userId);
        if(user == null){
            user = userRepository.findByUserId(userId);
            userCacheRepository.addUser(user);
        }

        return user;
    }

    public void addUser(UserCreateRequest userCreateRequest) throws JsonProcessingException {

//        user.setPassword(passwordEncoder.encode(user.getPassword()));
          User user = User.builder()
                  .userId(userCreateRequest.getUserId())
                  .name(userCreateRequest.getName())
                  .age(userCreateRequest.getAge())
                  .email(userCreateRequest.getEmail())
                  .phone(userCreateRequest.getPhone())
                  .authorities("usr")
                  .password(userCreateRequest.getPassword())
                  .build();
        userRepository.save(user);
        userCacheRepository.addUser(user);

        //TODO: PUBLISH KAFKA EVENT FOR USER_CREATE
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", user.getUserId());
        jsonObject.put("amount", AMOUNT);
        jsonObject.put("auditId", UUID.randomUUID().toString());
        kafkaTemplate.send(USER_CREATE_TOPIC, objectMapper.writeValueAsString(jsonObject));

        // make an entry in kafka_audit_log // time + service name
    }
}
