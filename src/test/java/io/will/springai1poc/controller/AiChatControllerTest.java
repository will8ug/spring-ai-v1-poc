package io.will.springai1poc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "PT30S")  // 30 seconds
public class AiChatControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testChat_thenReturnSuccessResponse() {
        webTestClient.post()
                .uri("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AiChatController.CustomChatRequest("This is a test message"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AiChatController.CustomChatResponse.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.content());
                    assertFalse(response.content().isEmpty());
                });
    }
}
