package ru.altarix.marm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class MarmApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarmApiApplication.class, args);
	}

}
