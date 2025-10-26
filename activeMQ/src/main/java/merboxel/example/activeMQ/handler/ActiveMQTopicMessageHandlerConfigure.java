package merboxel.example.activeMQ.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQTopicMessageHandlerConfigure {

    @Bean("customer1")
    public ActiveMQTopicMessageHandler Customer1TopicMessageHandler() {
        return new Customer1TopicMessageHandler();
    }

    @Bean("customer2")
    public ActiveMQTopicMessageHandler Customer2TopicMessageHandler() {
        return new Customer2TopicMessageHandler();
    }
}
