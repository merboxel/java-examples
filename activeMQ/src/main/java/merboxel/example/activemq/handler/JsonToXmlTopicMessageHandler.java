package merboxel.example.activemq.handler;

import jakarta.jms.Message;
import merboxel.example.activemq.helper.JmsJsonMessageReader;
import merboxel.example.activemq.mapper.xml.InboundJsonToOutboundXmlMapper;
import merboxel.example.activemq.message.InboundJsonMessage;
import merboxel.example.activemq.message.OutboundXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JsonToXmlTopicMessageHandler implements ActiveMQTopicMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(JsonToXmlTopicMessageHandler.class);

    public static final List<InboundJsonMessage> RECEIVED = new CopyOnWriteArrayList<>();

    private final JmsJsonMessageReader jmsJsonMessageReader;

    private final InboundJsonToOutboundXmlMapper mapper;

    private final RestClient restClient;

    public JsonToXmlTopicMessageHandler(JmsJsonMessageReader jmsJsonMessageReader, RestClient.Builder clientBuilder) {
        this.jmsJsonMessageReader = jmsJsonMessageReader;
        this.mapper = new InboundJsonToOutboundXmlMapper();
        this.restClient = clientBuilder.build();
    }

    @Override
    public void handleMessage(Message message, String outboundEndpoint) {
        log.info("[JsonToXml inbound] {}", message);

        InboundJsonMessage deserializedMessage = jmsJsonMessageReader.read(message, InboundJsonMessage.class);
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
