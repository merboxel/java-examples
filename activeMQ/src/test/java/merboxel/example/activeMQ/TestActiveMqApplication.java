package merboxel.example.activeMQ;

import org.springframework.boot.SpringApplication;

public class TestActiveMqApplication {

	public static void main(String[] args) {
		SpringApplication.from(ActiveMqApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
