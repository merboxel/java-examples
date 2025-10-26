package merboxel.example.activeMQ.handler;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActiveMQTopicMessageHandlerStrategy {

    private final Map<String, ActiveMQTopicMessageHandler> activeMQTopicMessageHandler;

    public ActiveMQTopicMessageHandlerStrategy(Map<String, ActiveMQTopicMessageHandler> topicHandlerMap) {
        this.activeMQTopicMessageHandler = topicHandlerMap;
    }

    public ActiveMQTopicMessageHandler getActiveMQTopicMessageHandler(String topicName) {
        return activeMQTopicMessageHandler.get(topicName);
    }
}
