package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    ModelDictionaryProvider dictionaryProvider;

	@Autowired
	ModelProvider modelProvider;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

        dictionaryProvider.getDictionary();

		modelProvider.getModel();

	}
}
