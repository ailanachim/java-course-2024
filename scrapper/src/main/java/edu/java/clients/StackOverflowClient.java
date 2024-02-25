package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {

    private final WebClient webClient;
    final static String BASE_URL = "https://api.stackexchange.com";

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public StackOverflowClient() {
        this(BASE_URL);
    }

    public OffsetDateTime getLastModifiedTime(String uri) {
        Mono<Response>
            responseMono = webClient.get().uri(uri).retrieve().bodyToMono(Response.class);
        return responseMono.block().modifiedTime();
    }

    record Response(@JsonProperty("items") Item[] items) {

        OffsetDateTime modifiedTime() {
            return items[0].modifiedTime();
        }

        record Item(@JsonProperty("last_edit_date") long dateTime) {
            OffsetDateTime modifiedTime() {
                return OffsetDateTime.of(LocalDateTime.ofEpochSecond(dateTime, 0, ZoneOffset.UTC), ZoneOffset.UTC);
            }
        }
    }

}
