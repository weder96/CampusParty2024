package dev.wsousa.sprb17;

import dev.wsousa.sprb17.service.AWSBedRockAgentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	AWSBedRockAgentService aWSBedRockAgentService = new AWSBedRockAgentService();
	@Bean
	public Function<String,String> reverse() {
	String response = aWSBedRockAgentService.converse("Why was 'David Marqui' a Hero?");
		return (s) -> response;
	}
}
