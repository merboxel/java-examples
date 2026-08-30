package merboxel.example.activemq.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class JsonMapperConfigure {

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder().build();
    }
}
