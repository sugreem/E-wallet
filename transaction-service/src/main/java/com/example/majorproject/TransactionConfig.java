package com.example.majorproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
// TODO: Extend from WebSecurityConfigureAdapter
public class TransactionConfig extends WebSecurityConfigurerAdapter {

    @Bean
        // Not necessary
    Properties getKafkaProps() {
        Properties properties = new Properties();

        // Producer related Props
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Consumer related Props
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return properties;
    }

    @Bean
        // Not necessary
    ProducerFactory<String, String> getProducerFactory() {
        return new DefaultKafkaProducerFactory(getKafkaProps());
    }

    @Bean
        // Necessary
    KafkaTemplate<String, String> getKafkaTemplate() {
        return new KafkaTemplate(getProducerFactory());
    }

    @Bean
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    ConsumerFactory<String, String> getConsumerFactory() {
        return new DefaultKafkaConsumerFactory(getKafkaProps());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(getConsumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    //TODO: For Spring Security
    // Authentication Bean
    // Authorization Bean
    // Password Encoder Bean


    @Autowired
    TransactionService transactionService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("In transaction config");
        auth.userDetailsService(transactionService);  //want to do authentication using this class userService
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()              //http.authorizeRequests()-->earlier without postman testing
                .and()
                .authorizeRequests()
                .antMatchers("/transaction/**").hasAuthority("usr")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin();
        http.cors().disable().csrf().disable();
    }

    @Bean
    PasswordEncoder getEncoder() {

        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }

}