package com.example.AplicatieMeditatii;

import com.example.AplicatieMeditatii.customer.Customer;
import com.example.AplicatieMeditatii.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class AplicatieMeditatiiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplicatieMeditatiiApplication.class, args);


	}
	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			var faker = new Faker();
			Random random = new Random();
			Name name = faker.name();
			String firstName = name.firstName();
			String lastName = name.lastName();
			Customer customer = new Customer(
					firstName + " " + lastName,
					firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
					random.nextInt(16, 99)
			);
			customerRepository.save(customer);
		};
	}
}
