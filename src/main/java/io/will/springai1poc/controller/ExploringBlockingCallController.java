package io.will.springai1poc.controller;

import io.will.springai1poc.service.QuickTestApisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exploring-blocking")
public class ExploringBlockingCallController {
    private final QuickTestApisService quickTestApisService;

    public ExploringBlockingCallController(QuickTestApisService quickTestApisService) {
        this.quickTestApisService = quickTestApisService;
    }

    @GetMapping("/blocking-after-creating-mono")
    public Mono<AiChatController.CustomChatResponse> blockingAfterCreatingMono() {
        return quickTestApisService
                .simpleChat("This is a test message. Please reply me if you are working.")
                .map(AiChatController.CustomChatResponse::withContent);
    }
}
