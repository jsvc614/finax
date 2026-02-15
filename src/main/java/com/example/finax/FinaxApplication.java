package com.example.finax;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class FinaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinaxApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx, Environment env, DataSource dataSource) {
		return args -> {

			String port = env.getProperty("server.port");
			System.out.println("Server is running on port: " + port);

			try (Connection conn = dataSource.getConnection()) {
				System.out.println("✅ Connected to database: " + conn.getMetaData().getURL());
				System.out.println("Database user: " + conn.getMetaData().getUserName());
			} catch (Exception e) {
				System.err.println("❌ Failed to connect to database: " + e.getMessage());
			}
		};
	}
}
