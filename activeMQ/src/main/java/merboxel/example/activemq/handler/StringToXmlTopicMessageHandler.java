package merboxel.example.activemq.handler;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import merboxel.example.activemq.mapper.xml.InboundStringToOutboundXmlMapper;
import merboxel.example.activemq.message.OutboundXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringToXmlTopicMessageHandler implements ActiveMQTopicMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(StringToXmlTopicMessageHandler.class);

    public static final List<String> RECEIVED = new CopyOnWriteArrayList<>();

    private final InboundStringToOutboundXmlMapper mapper;

    private final RestClient restClient;

    public StringToXmlTopicMessageHandler(RestClient.Builder clientBuilder) {
        this.mapper = new InboundStringToOutboundXmlMapper();
        this.restClient = clientBuilder.build();
    }

    @Override
    public void handleMessage(Message message, String outboundEndpoint) throws JMSException {
        log.info("[StringToXml inbound] {}", message);

        String deserializedMessage = message.getBody(String.class);
        RECEIVED.add(deserializedMessage);
        OutboundXmlMessage outboundMessage = mapper.map(deserializedMessage);

        String response = restClient.post()
                .uri(outboundEndpoint)
                .body(outboundMessage)
                .retrieve()
                .body(String.class);

        log.info("[JsonToXml outbound] resp: {}", response);
    }
}
