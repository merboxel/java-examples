package merboxel.example.activemq.mapper.xml;

import merboxel.example.activemq.message.OutboundXmlMessage;

public class InboundStringToOutboundXmlMapper {

    public OutboundXmlMessage map(String inboundStringMessage) {
        return new OutboundXmlMessage().setMessage(inboundStringMessage);
    }
}
