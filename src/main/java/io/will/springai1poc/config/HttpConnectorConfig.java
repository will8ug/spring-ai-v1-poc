package io.will.springai1poc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class HttpConnectorConfig {
    /**
     * Provides a RestClient.Builder bean for Spring AI's blocking RestClient calls.
     * Expect to be used in XXXChatAutoConfiguration.
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(20));
        requestFactory.setReadTimeout(Duration.ofSeconds(120));

        return RestClient.builder().requestFactory(requestFactory);
    }
}
