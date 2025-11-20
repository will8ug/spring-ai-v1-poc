package io.will.springai1poc.exception;

import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(ReadTimeoutException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleReadTimeoutException(
            ReadTimeoutException ex, ServerWebExchange exchange) {
        logger.error("Read timeout exception", ex);

        Map<String, Object> errorResponse = createErrorResponse(exchange, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    private Map<String, Object> createErrorResponse(ServerWebExchange exchange, Throwable ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("path", exchange.getRequest().getPath().value());
        errorResponse.put("method", exchange.getRequest().getMethod().name());
        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "Unknown error message");
        errorResponse.put("exception", ex.getClass().getSimpleName());

        return errorResponse;
    }
}
