package com.example.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);

	}

	@Bean
	Hibernate5JakartaModule hibernate5Module(){
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
		return hibernate5JakartaModule;
	}
}
