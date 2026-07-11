package com.vulnchain.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class VulnChainLabApplication extends  SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure( SpringApplicationBuilder builder){
		return builder.sources(VulnChainLabApplication.class);
	}

	public static void main(String[] args) {

		SpringApplication.run(VulnChainLabApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
