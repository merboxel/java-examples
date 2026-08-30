package merboxel.example.activemq.integration;

import merboxel.example.activemq.handler.JsonToJsonTopicMessageHandler;
import merboxel.example.activemq.handler.JsonToXmlTopicMessageHandler;
import merboxel.example.activemq.message.InboundJsonMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.Lifecycle;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
class DynamicActiveMQListenersIT {

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
        JsonToJsonTopicMessageHandler.RECEIVED.clear();
        JsonToXmlTopicMessageHandler.RECEIVED.clear();

        await().atMost(Duration.ofSeconds(10))
                .until(() -> registry.getListenerContainers().stream().allMatch(Lifecycle::isRunning));
    }

    @Test
    void testDynamicListenersReceiveMessages() {

        JsonMapper jsonMapper = new JsonMapper();

        int numberOfMessages = 10;

        // Publish 1000 messages
        IntStream.range(0,numberOfMessages).forEach(i -> {
            jmsTemplate.setMessageConverter(new SimpleMessageConverter());
            jmsTemplate.convertAndSend("example-topic-1", jsonMapper.writeValueAsString(new InboundJsonMessage().setMessage("topic-1 hello-"+i)));
            jmsTemplate.convertAndSend("example-topic-2", jsonMapper.writeValueAsString(new InboundJsonMessage().setMessage("topic-2 hello-"+i)));
        });

        // Wait for the listeners to consume messages
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    assertThat(JsonToJsonTopicMessageHandler.RECEIVED).size().isEqualTo(numberOfMessages);
                    assertThat(JsonToXmlTopicMessageHandler.RECEIVED).size().isEqualTo(numberOfMessages);
                });
    }
}
