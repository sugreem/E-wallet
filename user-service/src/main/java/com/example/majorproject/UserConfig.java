package com.example.majorproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


import java.util.Properties;

@Configuration
public class UserConfig {

    // To get redis connection
    @Bean  // Not necessary since it is used only in getTemplate function
    LettuceConnectionFactory getRedisFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(
                        "redis-19905.c212.ap-south-1-1.ec2.cloud.redislabs.com", 19905);
        redisStandaloneConfiguration.setPassword("Redis@123");
        LettuceConnectionFactory lettuceConnectionFactory =
                new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    // To get the template using which we will perform redis related ops
    @Bean // necessary
    RedisTemplate<String, Object> getTemplate(){
        System.out.println("In getTemplate function");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setConnectionFactory(getRedisFactory());
        return redisTemplate;
    }

    @Bean // Not necessary
    Properties getKafkaProps(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return properties;
    }

    @Bean // Not necessary
    ProducerFactory<String, String> getProducerFactory(){
        return new DefaultKafkaProducerFactory(getKafkaProps());
    }

    @Bean // Necessary
    KafkaTemplate<String, String> getKafkaTemplate(){
        return new KafkaTemplate(getProducerFactory());
    }

    @Bean
    ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

//    @Bean
//    PasswordEncoder getEncoder(){
//        return new BCryptPasswordEncoder();
//    } // used for password encode coming from login interface
}
