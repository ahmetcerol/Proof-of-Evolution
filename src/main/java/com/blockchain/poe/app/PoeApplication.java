package com.blockchain.poe.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blockchain.poe.config.BlockchainConfig;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Starts WebService and initializes SpringBoot framework.
 *
 * @author Ahmet Can EROL
 *
 */

@SpringBootApplication(scanBasePackageClasses = { BlockchainConfig.class })
public class PoeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoeApplication.class, args);
	}
}