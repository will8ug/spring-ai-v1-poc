package io.will.springai1poc.controller;

import io.will.springai1poc.service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        return aiChatService.chat(request.message()).map(ChatResponse::new);
    }

    @PostMapping("/reasoning")
    public Mono<ChatResponse> reasoning(@RequestBody ChatRequest request) {
        return aiChatService.reasoning(request.message()).map(ChatResponse::new);
    }

    public record ChatRequest(String message) {}
    public record ChatResponse(String content) {}
}
