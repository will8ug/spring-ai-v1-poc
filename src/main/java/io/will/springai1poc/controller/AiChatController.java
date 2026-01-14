package io.will.springai1poc.controller;

import io.will.springai1poc.service.DeepSeekChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AiChatController {

    private final DeepSeekChatService deepSeekChatService;

    public AiChatController(DeepSeekChatService deepSeekChatService) {
        this.deepSeekChatService = deepSeekChatService;
    }

    @PostMapping("/chat")
    public Mono<CustomChatResponse> chat(@RequestBody CustomChatRequest request) {
        return deepSeekChatService.chat(request.message()).map(CustomChatResponse::withContent);
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomChatResponse> chatStream(@RequestBody CustomChatRequest request) {
        return deepSeekChatService.chatStream(request.message());
    }

    @PostMapping("/reasoning")
    public Mono<CustomChatResponse> reasoning(@RequestBody CustomChatRequest request) {
        return deepSeekChatService.reasoning(request.message()).map(CustomChatResponse::withReasoningContent);
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
