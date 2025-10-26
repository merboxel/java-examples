package merboxel.example.activemq.listener;

import jakarta.annotation.PostConstruct;
import merboxel.example.activemq.config.ActiveMQFactoryProperties;
import merboxel.example.activemq.config.ActiveMQListenerProperties;
import merboxel.example.activemq.handler.ActiveMQTopicMessageHandlerStrategy;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;

import java.util.List;


@Configuration
public class DynamicListenerLoader {

    private static final Logger log = LoggerFactory.getLogger(DynamicListenerLoader.class);

    private final ActiveMQFactoryProperties activeMQFactoryProperties;

    private final ActiveMQListenerProperties listenerProperties;

    private final JmsListenerEndpointRegistry registry;

    private final ActiveMQTopicMessageHandlerStrategy topicMessageHandlerStrategy;

    public DynamicListenerLoader(ActiveMQFactoryProperties activeMQFactoryProperties,
                                    ActiveMQListenerProperties listenerProperties,
                                 JmsListenerEndpointRegistry registry,
                                 ActiveMQTopicMessageHandlerStrategy topicMessageHandlerStrategy) {
        this.activeMQFactoryProperties = activeMQFactoryProperties;
        this.listenerProperties = listenerProperties;
        this.registry = registry;
        this.topicMessageHandlerStrategy = topicMessageHandlerStrategy;
    }

    @PostConstruct
    public void registerListeners() {
        listenerProperties.getListener().forEach((key,config) -> {
            log.info("Registering listener name={}, clientId={}, topic={}, handler={}",
                    key, config.getClientId(), config.getDestination(), config.getHandlerBean());
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
                topicMessageHandlerStrategy.getActiveMQTopicMessageHandler(topicListenerConfig.getHandlerBean()).handleMessage(message, topicListenerConfig.getOutbound());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return endpoint;
    }

    private DefaultJmsListenerContainerFactory createFactory(ActiveMQListenerProperties.TopicListenerConfig topicListenerConfig) {
        // fresh connection per listener
        ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory();
        amqFactory.setTrustedPackages(List.of("merboxel.example.activemq"));
        amqFactory.setBrokerURL(activeMQFactoryProperties.getBrokerUrl());
        amqFactory.setUserName(activeMQFactoryProperties.getUsername());
        amqFactory.setPassword(activeMQFactoryProperties.getPassword());
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
