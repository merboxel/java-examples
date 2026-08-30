package merboxel.example.activemq;

import org.springframework.boot.SpringApplication;

public class TestActiveMqApplication {

	static void main(String[] args) {
		SpringApplication.from(ActiveMqApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
