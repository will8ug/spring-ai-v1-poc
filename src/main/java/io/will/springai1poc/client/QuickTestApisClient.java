package io.will.springai1poc.client;

import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface QuickTestApisClient {
    @GetExchange("/ping")
    Mono<String> ping();
}
