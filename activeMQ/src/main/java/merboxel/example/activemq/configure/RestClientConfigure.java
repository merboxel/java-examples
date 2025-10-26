package merboxel.example.activemq.configure;

import merboxel.example.activemq.helper.InterceptOnlyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientConfigure {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestInterceptor(new InterceptOnlyInterceptor());
    }
}
