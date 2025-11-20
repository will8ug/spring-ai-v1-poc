package io.will.springai1poc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiChatService {
    private final Logger logger = LoggerFactory.getLogger(AiChatService.class);

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

    public Mono<String> reasoning(String query) {
        return Mono.fromCallable(() -> {
                    ChatResponse response = chatClient.prompt()
                            .user(query)
                            .call()
                            .chatResponse();
                    DeepSeekAssistantMessage message = (DeepSeekAssistantMessage) response.getResult().getOutput();
                    logger.info("response text: {}", message.getText());
                    return String.valueOf(message.getReasoningContent());
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
