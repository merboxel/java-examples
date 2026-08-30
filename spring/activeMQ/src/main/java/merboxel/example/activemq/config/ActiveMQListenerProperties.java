package merboxel.example.activemq.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "activemq")
public class ActiveMQListenerProperties {

    private Map<String, TopicListenerConfig> listener = new HashMap<>();

    @PostConstruct
    public void validateConfig() {

        assert (listener != null);
        assert (listener.size()
                ==
                listener.values().stream().map(TopicListenerConfig::getClientId)
                        .distinct()
                        .count()) : "all 'activemq.listener.[].client_id' must be unique";
        assert (listener.size()
                ==
                listener.values().stream().map(TopicListenerConfig::getSubscription)
                        .distinct()
                        .count()) : "all 'activemq.listener.[].subscription' must be unique";

    }

    public Map<String, TopicListenerConfig> getListener() {
        return listener;
    }
    public void setListener(Map<String, TopicListenerConfig> topic) {
        this.listener = topic;
    }

    public static class TopicListenerConfig {

        private String clientId;
        private String subscription;
        private String destination;
        private String handlerBean;
        private String outbound;

        public String getClientId() {
            return clientId;
        }
        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
        public String getSubscription() {
            return subscription;
        }
        public void setSubscription(String subscription) {
            this.subscription = subscription;
        }
        public String getDestination() {
            return destination;
        }
        public void setDestination(String destination) {
            this.destination = destination;
        }
        public String getHandlerBean() {
            return handlerBean;
        }
        public void setHandlerBean(String handlerBean) {
            this.handlerBean = handlerBean;
        }
        public String getOutbound() {
            return outbound;
        }
        public void setOutbound(String outbound) {
            this.outbound = outbound;
        }
    }
}
