package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {

    private final WebClient webClient;
    final static String BASE_URL = "https://api.stackexchange.com/";

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public StackOverflowClient() {
        this(BASE_URL);
    }

    public OffsetDateTime getLastModifiedTime(String uri) {
        ResponseEntity<Response> response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(uri)
                .queryParam("site", "stackoverflow")
                .build())
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
