package com.blockchain.poe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Basic configuration for the application.
 *
 * @author Ahmet Can EROL
 *
 */
@Configuration
@ComponentScan({ "com.blockchain.poe.service", "com.blockchain.poe.api", "com.blockchain.poe.config" })
public class BlockchainConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}