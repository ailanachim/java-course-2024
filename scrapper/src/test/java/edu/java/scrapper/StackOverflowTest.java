package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.clients.StackOverflowClient;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

public class StackOverflowTest {

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
        String url = "/questions/32435456";
        stubFor(get(url)
            .willReturn(ok()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody("""
                    {"items":
                    [{
                    "tags":["android","push-notification","android-notifications"],
                    "owner":{"account_id":4738341,"reputation":3895,"user_id":3832013,"user_type":"registered","accept_rate":96},
                    "post_state":"Published",
                    "is_answered":false,
                    "view_count":73,
                    "answer_count":0,
                    "score":2,
                    "last_activity_date":1441622847,
                    "creation_date":1441618627,
                    "last_edit_date":1495541680,
                    "question_id":32435456,
                    "content_license":"CC BY-SA 3.0",
                    "link":"https://stackoverflow.com/questions/32435456/android-recognize-if-notifications-for-app-disabled-from-apps-menu",
                    "title":"Android: Recognize if notifications for app disabled from apps menu"
                    }]
                    }
                    """)));

        StackOverflowClient client = new StackOverflowClient(wireMockServer.baseUrl());
        var time = client.getLastModifiedTime(url);
        var actual = OffsetDateTime.of(LocalDateTime.ofEpochSecond(1495541680, 0, ZoneOffset.UTC), ZoneOffset.UTC);
        assertThat(time).isEqualTo(actual);
    }
}
