package merboxel.example.activemq.mapper.json;

import merboxel.example.activemq.message.OutboundJsonMessage;

public class InboundStringToOutboundJsonMapper {

    public OutboundJsonMessage map(String inboundStringMessage) {
        return new OutboundJsonMessage().setMessage(inboundStringMessage);
    }
}
