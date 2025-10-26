package merboxel.example.activemq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ActiveMqApplicationTests {

    // Override Spring Boot's ActiveMQ connection dynamically
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.activemq.broker-url", () -> "url.dummy");
        registry.add("spring.activemq.user", () -> "admin");
        registry.add("spring.activemq.password", () -> "admin");
    }

	@Test
	void contextLoads() {
        assert(true);
    }

}
