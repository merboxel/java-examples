package merboxel.example.activeMQ.integration;

import merboxel.example.activeMQ.handler.Customer1TopicMessageHandler;
import merboxel.example.activeMQ.handler.Customer2TopicMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.Lifecycle;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
public class DynamicActiveMQListenersIT {

    @Container
    static final ActiveMQContainer activeMQ = new ActiveMQContainer("apache/activemq-classic:latest");

    // Override Spring Boot's ActiveMQ connection dynamically
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.activemq.broker-url", activeMQ::getBrokerUrl);
        registry.add("spring.activemq.user", () -> "admin");
        registry.add("spring.activemq.password", () -> "admin");
    }

    @Autowired
    private JmsListenerEndpointRegistry registry;

    @Autowired
    private JmsTemplate jmsTemplate;

    @BeforeEach
    void clearHandlers() {
        Customer1TopicMessageHandler.RECEIVED.clear();
        Customer2TopicMessageHandler.RECEIVED.clear();

        await().atMost(Duration.ofSeconds(10))
                .until(() -> registry.getListenerContainers().stream().allMatch(Lifecycle::isRunning));
    }

    @Test
    void testDynamicListenersReceiveMessages() {

        int numberOfMessages = 1000;

        // Publish 1000 messages
        IntStream.range(0,numberOfMessages).forEach(i -> {
            jmsTemplate.convertAndSend("example-topic-1", "topic-1 hello-"+i);
            jmsTemplate.convertAndSend("example-topic-2", "topic-2 hello-"+i);
        });

        // Wait for the listeners to consume messages
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    assertThat(Customer1TopicMessageHandler.RECEIVED).size().isEqualTo(numberOfMessages);
                    assertThat(Customer2TopicMessageHandler.RECEIVED).size().isEqualTo(numberOfMessages);
                });
    }
}
