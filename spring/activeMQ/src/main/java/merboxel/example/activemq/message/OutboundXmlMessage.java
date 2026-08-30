package merboxel.example.activemq.message;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "outboundMessage")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutboundXmlMessage {

    private  String message;

    public String getMessage() {
        return message;
    }

    public OutboundXmlMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}
