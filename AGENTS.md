# AGENTS.md

This file contains guidelines and commands for agentic coding agents working in this Spring AI project.

## Project Overview

This is a Spring Boot 3.5.7 application with Java 25 that integrates Spring AI 1.1.2 with DeepSeek for AI chat functionality. The project uses reactive programming with Spring WebFlux and Maven for build management.

## Build and Development Commands

### Maven Commands
```bash
# Build and compile
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ClassName

# Run specific test method
./mvnw test -Dtest=ClassName#methodName

# Build without tests
./mvnw clean package -DskipTests

# Generate test coverage report
./mvnw test jacoco:report

# View dependency tree
./mvnw dependency:tree

# Download source dependencies for debugging
./mvnw dependency:sources
```

### Running Single Tests
```bash
# Controller tests
./mvnw test -Dtest=AiChatControllerTest
./mvnw test -Dtest=HealthControllerTest

# Service tests (if added)
./mvnw test -Dtest=DeepSeekChatServiceTest
```

## Code Style Guidelines

### Package Structure
- Base package: `io.will.springai1poc`
- All lowercase with descriptive names
- Follow reverse domain convention

### Class Naming Conventions
- Controllers: `*Controller` suffix
- Services: `*Service` suffix  
- Configurations: `*Configuration` suffix
- Exceptions: `*ExceptionHandler` suffix
- Clients: `*Client` suffix
- DTOs: Use records with descriptive names

### Import Organization
```java
// 1. Package declaration
package io.will.springai1poc.controller;

// 2. Internal imports (same project)
import io.will.springai1poc.service.DeepSeekChatService;

// 3. Spring/Third-party imports
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

// 4. Java standard library
import reactor.core.publisher.Mono;
```

### Dependency Injection
- Use constructor injection only
- No `@Autowired` on fields
- Mark constructors with `@Autowired` if multiple constructors

### Reactive Programming Patterns
- Return `Mono<T>` for single async values
- Return `Flux<T>` for streams of values
- Use `subscribeOn(Schedulers.boundedElastic())` for blocking operations
- Proper error handling with reactive operators

### Record Usage for DTOs
```java
public record CustomChatRequest(String message) {}

public record CustomChatResponse(String content, String reasoningContent) {
    public static CustomChatResponse withContent(String content) {
        return new CustomChatResponse(content, null);
    }
    
    public static CustomChatResponse withReasoningContent(String reasoningContent) {
        return new CustomChatResponse(null, reasoningContent);
    }
}
```

## Error Handling Guidelines

### Global Exception Handling
- Use `@RestControllerAdvice` for global exception handlers
- Handle specific exceptions (e.g., `ReadTimeoutException`)
- Return structured error responses with request context

### Error Response Format
```java
// Return structured error responses
{
  "path": "/chat",
  "method": "POST",
  "message": "Error description", 
  "exception": "ExceptionClassName"
}
```

### Reactive Error Handling
- Use `Mono<ResponseEntity<T>>` for async error responses
- Proper exception propagation through reactive streams
- Handle timeouts and connection errors appropriately

## Testing Guidelines

### Test Structure
- Use `@SpringBootTest` for integration tests
- Use `@WebFluxTest` for controller slice tests
- Configure timeouts: `@AutoConfigureWebTestClient(timeout = "PT30S")`

### Test Naming
- Test classes: `*Test` suffix
- Test methods: descriptive names using `should_` or `when_` pattern
- Use `@Test` annotation from JUnit 5

### Reactive Testing
- Use `WebTestClient` for HTTP endpoint testing
- Use `StepVerifier` for reactive stream testing
- Properly dispose of reactive streams in tests

## Spring AI Integration Patterns

### ChatClient Configuration
- Use advisors for logging and memory management
- Configure message window chat memory (default: 10 messages)
- DeepSeek model integration with proper API key management

### Service Layer Patterns
- Wrap blocking AI calls in reactive types
- Use proper schedulers for blocking operations
- Support streaming responses for real-time interactions

## HTTP Client Configuration

### Reactive vs Blocking
- Use `WebClient` for reactive operations
- Use `RestClient` for blocking operations
- Configure custom timeouts appropriately

### Timeout Configuration
- Set reasonable connection and read timeouts
- Handle timeout exceptions gracefully
- Consider retry logic for external API calls

## Configuration Management

### Application Configuration
- Use `application.yaml` for configuration
- Support environment-specific properties
- Manage API keys through configuration, not code

### Bean Configuration
- Use `@Configuration` classes for bean definitions
- Use `@Bean` methods with proper return types
- Consider conditional bean creation with `@ConditionalOnProperty`

## Logging Guidelines

### Logging Framework
- Use SLF4J with Logback
- Declare logger: `private static final Logger log = LoggerFactory.getLogger(ClassName.class);`

### Log Levels
- `ERROR`: Exception handling, critical failures
- `WARN`: Deprecated usage, performance issues
- `INFO`: Important business events, startup/shutdown
- `DEBUG`: Detailed execution flow
- `TRACE`: Very detailed debugging information

## Security Considerations

### API Key Management
- Never commit API keys to version control
- Use environment variables or configuration files
- Consider Spring Cloud Config for centralized management

### Input Validation
- Validate all user inputs
- Sanitize data before processing
- Use proper content types and encoding

## Performance Guidelines

### Reactive Best Practices
- Avoid blocking calls in reactive chains
- Use proper schedulers for CPU-intensive operations
- Consider backpressure for streaming operations

### Memory Management
- Be mindful of memory usage with large responses
- Use streaming for large data sets
- Properly dispose of reactive subscriptions

## Common Patterns to Follow

### Controller Pattern
```java
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AiChatController {
    private final DeepSeekChatService chatService;
    
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CustomChatResponse> chat(@RequestBody CustomChatRequest request) {
        return chatService.chat(request.message());
    }
}
```

### Service Pattern
```java
@Service
@RequiredArgsConstructor
public class DeepSeekChatService {
    private final ChatClient chatClient;
    
    public Mono<CustomChatResponse> chat(String message) {
        return Mono.fromCallable(() -> chatClient.call(message))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
```

### Configuration Pattern
```java
@Configuration
public class AiModelConfiguration {
    
    @Bean
    public ChatClient chatClient(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultAdvisors(advisors -> advisors
                        .new PromptChatMemoryAdvisor(10))
                .build();
    }
}
```

## Tools and IDE Integration

### Recommended IDE Settings
- Enable annotation processing
- Configure code formatting based on project standards
- Set up proper import organization

### Linting and Code Quality
- Use Spring Boot's built-in validation
- Consider adding SpotBugs or PMD for additional code quality checks
- Use SonarQube for code quality analysis if available