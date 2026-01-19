package io.will.springai1poc.service;

import io.will.springai1poc.client.QuickTestApisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class QuickTestApisService {
    private final Logger logger = LoggerFactory.getLogger(QuickTestApisService.class);

    private final ChatClient chatClient;
    private final QuickTestApisClient quickTestApisClient;

    public QuickTestApisService(QuickTestApisClient quickTestApisClient,
                                ChatClient chatClient) {
        this.quickTestApisClient = quickTestApisClient;
        this.chatClient = chatClient;
    }

    public Mono<String> simpleChat(String query) {
        return quickTestApisClient.ping()
                .map(pingMsg -> {
                    logger.info("Ping response: {}", pingMsg);

                    ChatResponse chatResponse = chatClient.prompt()
                            .user(query)
                            .call()
                            .chatResponse();
                    assert chatResponse != null;
                    return chatResponse.getResult().getOutput().getText();
                });
    }
}
