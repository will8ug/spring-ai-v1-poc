package io.will.springai1poc.service;

import io.will.springai1poc.controller.AiChatController.CustomChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class DeepSeekChatService {
    private final Logger logger = LoggerFactory.getLogger(DeepSeekChatService.class);

    private final ChatClient chatClient;

    public DeepSeekChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Mono<String> chat(String query) {
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(query)
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> reasoning(String query) {
        return Mono.fromCallable(() -> {
                    var response = chatClient.prompt()
                            .user(query)
                            .call()
                            .chatResponse();
                    DeepSeekAssistantMessage message = (DeepSeekAssistantMessage) response.getResult().getOutput();
                    logger.info("response text: {}", message.getText());
                    return String.valueOf(message.getReasoningContent());
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<CustomChatResponse> chatStream(String query) {
        var chatResponse = chatClient.prompt()
                .user(query)
                .stream()
                .chatResponse();

        return chatResponse.map(chunk -> {
            var message = (DeepSeekAssistantMessage) chunk.getResult().getOutput();
            return new CustomChatResponse(message.getText(), message.getReasoningContent());
        });
    }
}
