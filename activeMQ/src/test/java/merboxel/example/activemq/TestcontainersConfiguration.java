package merboxel.example.activemq;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	ActiveMQContainer activemqContainer() {
		return new ActiveMQContainer(DockerImageName.parse("apache/activemq-classic:latest"));
	}

}
