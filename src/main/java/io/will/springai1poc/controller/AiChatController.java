package io.will.springai1poc.controller;

import io.will.springai1poc.service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public Mono<CustomChatResponse> chat(@RequestBody CustomChatRequest request) {
        return aiChatService.chat(request.message()).map(CustomChatResponse::withContent);
    }

    @PostMapping("/chat/stream")
    public Flux<CustomChatResponse> chatStream(@RequestBody CustomChatRequest request) {
        return aiChatService.chatStream(request.message());
    }

    @PostMapping("/reasoning")
    public Mono<CustomChatResponse> reasoning(@RequestBody CustomChatRequest request) {
        return aiChatService.reasoning(request.message()).map(CustomChatResponse::withReasoningContent);
    }

    public record CustomChatRequest(String message) {}

    public record CustomChatResponse(String content, String reasoningContent) {
        public static CustomChatResponse withContent(String content) {
            return new CustomChatResponse(content, "");
        }

        public static CustomChatResponse withReasoningContent(String reasoningContent) {
            return new CustomChatResponse("", reasoningContent);
        }
    }
}
