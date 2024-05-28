package com.easysplit.ess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.easysplit.shared.infrastructure"})
@ComponentScan(basePackages = {"com.easysplit.shared.domain"})
@ComponentScan(basePackages = {"com.easysplit.shared.utils"})
@SpringBootApplication
public class EssApplication {

	public static void main(String[] args) {
		SpringApplication.run(EssApplication.class, args);
	}

}
