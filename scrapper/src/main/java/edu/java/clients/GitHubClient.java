package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

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
        ResponseEntity<Response> response = webClient.get()
            .uri(uri)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                ClientResponse::createError
            )
            .toEntity(Response.class)
            .block();

        long millis = response.getHeaders().getLastModified();
        if (millis != -1) {
            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
        }

        return response.getBody().modifiedTime();
    }

    record Response(@JsonProperty("updated_at") OffsetDateTime modifiedTime) {
    }
}
