package merboxel.example.activemq.handler;

import jakarta.jms.JMSException;
import jakarta.jms.Message;

public interface ActiveMQTopicMessageHandler {
    void handleMessage(Message message, String outboundEndpoint) throws JMSException;
}
