package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {

    private final WebClient webClient;
    final static String BASE_URL = "https://api.github.com";

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public GitHubClient() {
        this(BASE_URL);
    }

    public OffsetDateTime getLastModifiedTime(String uri) {
        Mono<Response> responseMono = webClient.get().uri(uri).retrieve().bodyToMono(Response.class);
        return responseMono.block().modifiedTime();
    }

    record Response(@JsonProperty("updated_at") OffsetDateTime modifiedTime) {
    }
}
