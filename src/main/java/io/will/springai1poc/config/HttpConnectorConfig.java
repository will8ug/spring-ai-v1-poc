package io.will.springai1poc.config;

import io.will.springai1poc.client.QuickTestApisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class HttpConnectorConfig {
    private static final String QUICK_TEST_APIS_URL = "http://localhost:10001";

    /**
     * Provides a RestClient.Builder bean for Spring AI's blocking RestClient calls.
     * Expect to be used in XXXChatAutoConfiguration, such as DeepSeekChatAutoConfiguration.
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(20));
        requestFactory.setReadTimeout(Duration.ofSeconds(120));

        return RestClient.builder().requestFactory(requestFactory);
    }

    @Bean
    public QuickTestApisClient quickTestApisClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(QUICK_TEST_APIS_URL)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().responseTimeout(Duration.ofSeconds(10))
                ))
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(QuickTestApisClient.class);
    }
}
