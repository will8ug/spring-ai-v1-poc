package io.will.springai1poc.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiChatService {
    private final ChatClient chatClient;

    public AiChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Mono<String> chat(String query) {
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(query)
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
