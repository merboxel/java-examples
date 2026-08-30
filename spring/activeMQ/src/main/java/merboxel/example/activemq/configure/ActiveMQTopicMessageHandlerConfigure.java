package merboxel.example.activemq.configure;

import merboxel.example.activemq.handler.*;
import merboxel.example.activemq.helper.JmsJsonMessageReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ActiveMQTopicMessageHandlerConfigure {

    @Bean("jsonToJson")
    public ActiveMQTopicMessageHandler jsonToJsonTopicMessageHandler(JmsJsonMessageReader jmsJsonMessageReader, RestClient.Builder clientBuilder) {
        return new JsonToJsonTopicMessageHandler(jmsJsonMessageReader, clientBuilder);
    }

    @Bean("jsonToXml")
    public ActiveMQTopicMessageHandler jsonToXmlTopicMessageHandler(JmsJsonMessageReader jmsJsonMessageReader, RestClient.Builder clientBuilder) {
        return new JsonToXmlTopicMessageHandler(jmsJsonMessageReader, clientBuilder);
    }

    @Bean("stringToJson")
    public ActiveMQTopicMessageHandler stringToJsonTopicMessageHandler(RestClient.Builder clientBuilder) {
        return new StringToJsonTopicMessageHandler(clientBuilder);
    }

    @Bean("stringToXml")
    public ActiveMQTopicMessageHandler stringToXmlTopicMessageHandler(RestClient.Builder clientBuilder) {
        return new StringToXmlTopicMessageHandler(clientBuilder);
    }
}
