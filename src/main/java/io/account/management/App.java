package io.account.management;

import io.account.management.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class App {

	public static final String VERSION = "v1";

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);


	}

}
