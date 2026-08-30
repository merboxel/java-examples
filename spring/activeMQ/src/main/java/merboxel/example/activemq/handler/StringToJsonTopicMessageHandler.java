package merboxel.example.activemq.handler;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import merboxel.example.activemq.mapper.json.InboundStringToOutboundJsonMapper;
import merboxel.example.activemq.message.OutboundJsonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringToJsonTopicMessageHandler implements ActiveMQTopicMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(StringToJsonTopicMessageHandler.class);

    public static final List<String> RECEIVED = new CopyOnWriteArrayList<>();

    private final InboundStringToOutboundJsonMapper mapper;

    private final RestClient restClient;

    public StringToJsonTopicMessageHandler(RestClient.Builder clientBuilder) {
        this.mapper = new InboundStringToOutboundJsonMapper();
        this.restClient = clientBuilder.build();
    }

    @Override
    public void handleMessage(Message message, String outboundEndpoint) throws JMSException {
        log.info("[StringToJson inbound] {}", message);

        String deserializedMessage = message.getBody(String.class);
        RECEIVED.add(deserializedMessage);
        OutboundJsonMessage outboundMessage = mapper.map(deserializedMessage);

        String response = restClient.post()
                .uri(outboundEndpoint)
                .body(outboundMessage)
                .retrieve()
                .body(String.class);

        log.info("[StringToJson outbound] resp: {}", response);
    }
}
