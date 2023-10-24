package com.blockchain.poe.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blockchain.poe.config.BlockchainConfig;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This project was created using IntelliJ IDEA in 2023 with JDK version 21.
 * <br/>
 * It starts a blockchain with APIs using Spring Boot.
 * <br/>
 * Author: Ahmet Can EROL
 */


/*
This code sets the main class for a Spring Boot application and instructs it to scan
for components in the specified package, which includes the BlockchainConfig
class and its associated package
 */
@SpringBootApplication(scanBasePackageClasses = {BlockchainConfig.class})
public class PoeApplication {public static void main(String[] args) {SpringApplication.run(PoeApplication.class, args);}}