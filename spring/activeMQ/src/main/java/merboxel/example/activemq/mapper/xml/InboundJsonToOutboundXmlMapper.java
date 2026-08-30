package merboxel.example.activemq.mapper.xml;

import merboxel.example.activemq.message.InboundJsonMessage;
import merboxel.example.activemq.message.OutboundXmlMessage;

public class InboundJsonToOutboundXmlMapper {

    public OutboundXmlMessage map(InboundJsonMessage inboundJsonMessage) {
        return new OutboundXmlMessage().setMessage(inboundJsonMessage.getMessage());
    }
}
