package merboxel.example.activemq.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperConfigure {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
