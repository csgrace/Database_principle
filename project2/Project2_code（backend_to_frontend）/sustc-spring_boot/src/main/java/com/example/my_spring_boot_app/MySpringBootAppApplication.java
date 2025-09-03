package com.example.my_spring_boot_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.my_spring_boot_app", "io.pubmed.service.impl"})//
public class MySpringBootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringBootAppApplication.class, args);
	}
}
