package merboxel.example.activemq.helper;

import jakarta.jms.Message;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class JmsJsonMessageReader {

    private final JsonMapper mapper;

    public JmsJsonMessageReader(JsonMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T read(Message message, Class<T> clazz) {
        try {
            String json = message.getBody(String.class);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize JMS message to " + clazz.getSimpleName(), e);
        }
    }
}