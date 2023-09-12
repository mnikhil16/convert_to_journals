package com.erp.convert_to_journals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConvertToJournalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConvertToJournalsApplication.class, args);
	}

}
