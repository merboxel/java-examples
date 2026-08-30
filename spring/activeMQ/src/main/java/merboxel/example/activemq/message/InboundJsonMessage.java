package merboxel.example.activemq.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InboundJsonMessage {

    public static final String JSON_PROPERTY_MESSAGE = "message";

    @JsonProperty(JSON_PROPERTY_MESSAGE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    private String message;

    public String getMessage() {
        return message;
    }

    public InboundJsonMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}
