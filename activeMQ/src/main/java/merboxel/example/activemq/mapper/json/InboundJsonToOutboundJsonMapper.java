package merboxel.example.activemq.mapper.json;

import merboxel.example.activemq.message.InboundJsonMessage;
import merboxel.example.activemq.message.OutboundJsonMessage;

public class InboundJsonToOutboundJsonMapper {

    public OutboundJsonMessage map(InboundJsonMessage inboundJsonMessage) {
        return new OutboundJsonMessage().setMessage(inboundJsonMessage.getMessage());
    }
}
