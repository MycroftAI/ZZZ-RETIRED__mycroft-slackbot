package slackbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableConfigurationProperties
@Component
public class SlackbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackbotApplication.class, args);
	}
}