package merboxel.example.activemq.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InterceptOnlyInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(InterceptOnlyInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) {

        String sanitizedBody = new String(body, StandardCharsets.UTF_8);

        log.info("=== Intercepted REST request ===");
        log.info("URL: {}", request.getURI());
        log.info("Method: {}", request.getMethod());
        log.info("Headers: {}", request.getHeaders());
        log.info("Body: {}", sanitizedBody);

        log.info("Blocking request — not sending to server.");
        // Return a dummy response (you can mock anything you like)
        return new FakeClientHttpResponse("Intercepted response", 200);
    }

    public record FakeClientHttpResponse(String body, HttpStatus status) implements ClientHttpResponse {

            public FakeClientHttpResponse(String body, int status) {
                this(body, HttpStatus.valueOf(status));
            }

            @Override
            public HttpStatus getStatusCode() {
                return status;
            }

            @Override
            public String getStatusText() {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
                // Nothing to close
            }

            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(body.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        }
}