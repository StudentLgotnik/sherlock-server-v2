package com.sherlock.gateway.filter.validator;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("sherlock.identity.com")
public class SherlockTokenValidator implements TokenValidator {

    private final WebClient.Builder webClientBuilder;

    public SherlockTokenValidator(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Boolean> isValid(String token) {
        return webClientBuilder.build()
                .post()
                .uri("http://sherlock-identity/api/v1/token/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
