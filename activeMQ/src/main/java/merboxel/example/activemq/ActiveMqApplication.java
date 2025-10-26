package merboxel.example.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ActiveMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveMqApplication.class, args);
	}

}
