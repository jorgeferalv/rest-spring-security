package com.formacion;

import com.formacion.model.Customer;
import com.formacion.repository.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class SpringSecurityApiApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringSecurityApiApplication.class, args);

		var repo = ctx.getBean(CustomerRepository.class);
		repo.saveAll(List.of(
			new Customer("customer1", "cust1@email", 4000.0, 25, false, "admin", "1234"),
			new Customer("customer2", "cust2@email", 5000.0, 45, true, "admin2", "5678"),
			new Customer("customer3", "cust3@email", 6000.0, 55, false, "admin3", "3523"),
			new Customer("customer4", "cust4@email", 7000.0, 35, true, "admin4", "7658")
		));
	}

}
