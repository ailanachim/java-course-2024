package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.clients.GitHubClient;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class GitHubTest {

    static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

    @BeforeAll
    static void wireMockInit() {
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void wireMockClose() {
        wireMockServer.stop();
    }

    @Test
    void test() {
        String url = "/repos/pengrad/java-telegram-bot-api";
        stubFor(get(url)
            .willReturn(ok()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody("""
                    {
                        "id": 40182023,
                        "node_id": "MDEwOlJlcG9zaXRvcnk0MDE4MjAyMw==",
                        "name": "java-telegram-bot-api",
                        "full_name": "pengrad/java-telegram-bot-api",
                        "private": false,
                        "created_at": "2015-08-04T11:58:56Z",
                        "updated_at": "2024-02-24T18:21:20Z",
                        "pushed_at": "2024-02-23T09:26:58Z"
                    }
                    """)));

        GitHubClient client = new GitHubClient(wireMockServer.baseUrl());
        assertThat(client.getLastModifiedTime(url)).isEqualTo(OffsetDateTime.parse(
            "2024-02-24T18:21:20Z"));
    }
}
