package io.account.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static final String VERSION = "v1";

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
