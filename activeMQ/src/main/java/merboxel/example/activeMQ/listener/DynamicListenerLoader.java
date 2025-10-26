package merboxel.example.activeMQ.listener;

import jakarta.annotation.PostConstruct;
import merboxel.example.activeMQ.config.ActiveMQListenerProperties;
import merboxel.example.activeMQ.handler.ActiveMQTopicMessageHandlerStrategy;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;


@Configuration
public class DynamicListenerLoader {

    private static final Logger log = LoggerFactory.getLogger(DynamicListenerLoader.class);

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    private final ActiveMQListenerProperties listenerProperties;

    private final JmsListenerEndpointRegistry registry;

    private final ActiveMQTopicMessageHandlerStrategy topicMEssageHandlerStrategy;

    public DynamicListenerLoader(ActiveMQListenerProperties listenerProperties,
                                 JmsListenerEndpointRegistry registry,
                                 ActiveMQTopicMessageHandlerStrategy topicMEssageHandlerStrategy) {
        this.listenerProperties = listenerProperties;
        this.registry = registry;
        this.topicMEssageHandlerStrategy = topicMEssageHandlerStrategy;
    }

    @PostConstruct
    public void registerListeners() {
        listenerProperties.getListener().forEach(config -> {
            log.info("Registering listener clientId={}, topic={}, handler={}",
                    config.getClientId(), config.getDestination(), config.getHandlerBean());
            registerDynamicListener(config);
        });
    }

    private void registerDynamicListener(ActiveMQListenerProperties.TopicListenerConfig topicListenerConfig) {

        SimpleJmsListenerEndpoint endpoint = createEndpoint(topicListenerConfig);

        // Create a unique listener container factory per listener
        DefaultJmsListenerContainerFactory factory = createFactory(topicListenerConfig);

        // Register and start the listener
        registry.registerListenerContainer(endpoint, factory, true);
    }

    private SimpleJmsListenerEndpoint createEndpoint(ActiveMQListenerProperties.TopicListenerConfig topicListenerConfig) {

        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(topicListenerConfig.getClientId() + "-endpoint");
        endpoint.setDestination(topicListenerConfig.getDestination());
        endpoint.setSubscription(topicListenerConfig.getSubscription());
        endpoint.setMessageListener(message -> {
            try {
                String text = message.getBody(String.class);
                topicMEssageHandlerStrategy.getActiveMQTopicMessageHandler(topicListenerConfig.getHandlerBean()).handleMessage(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return endpoint;
    }

    private DefaultJmsListenerContainerFactory createFactory(ActiveMQListenerProperties.TopicListenerConfig topicListenerConfig) {
        // fresh connection per listener
        ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory();
        amqFactory.setBrokerURL(brokerUrl);
        amqFactory.setUserName(username);
        amqFactory.setPassword(password);
        amqFactory.setClientID(topicListenerConfig.getClientId()); // unique per listener

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(amqFactory);
        factory.setPubSubDomain(true);
        factory.setSubscriptionDurable(true); // durable subscription
        factory.setCacheLevelName("CACHE_NONE"); // no shared connection
        factory.setErrorHandler(Throwable::printStackTrace);
        factory.setConcurrency("1-1");
        return factory;
    }
}
