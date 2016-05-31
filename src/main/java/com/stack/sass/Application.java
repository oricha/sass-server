package com.stack.sass;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
/**
 * Run SpringBoot App
 * mvn package
 * mvn spring-boot:run   o java -jar target/sass-converter-0.0.1-SNAPSHOT.jar
 *
 * 
 * @author kmuniz
 *
 */
@SpringBootApplication
public class Application {

	public static String ROOT = "upload-dir";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return (String[] args) -> {
			new File(ROOT).mkdir();
		};
	}
}
