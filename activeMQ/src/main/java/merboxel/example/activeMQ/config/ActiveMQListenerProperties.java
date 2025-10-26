package merboxel.example.activeMQ.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "activemq")
public class ActiveMQListenerProperties {

    private List<TopicListenerConfig> listener;

    public List<TopicListenerConfig> getListener() {
        return listener;
    }

    public void setListener(List<TopicListenerConfig> listener) {
        this.listener = listener;
    }

    public static class TopicListenerConfig {
        private String clientId;
        private String subscription;
        private String destination;
        private String handlerBean;

        // Getters & setters
        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }
        public String getSubscription() { return subscription; }
        public void setSubscription(String subscription) { this.subscription = subscription; }
        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public String getHandlerBean() { return handlerBean; }
        public void setHandlerBean(String handlerBean) { this.handlerBean = handlerBean; }
    }
}
