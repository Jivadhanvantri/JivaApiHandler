package com.jiva.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class JivaApiHandlerApplication implements CommandLineRunner {

	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(JivaApiHandlerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("DATASOURCE = " + dataSource);

	}

}
