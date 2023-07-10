package com.sherlock.gateway.filter.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component("https://accounts.google.com")
public class GoogleTokenValidator implements TokenValidator {

    private static final String GOOGLE_TOKEN_URI = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";

    @Override
    public Mono<Boolean> isValid(String token) {
        return WebClient.create()
                .get()
                .uri(URI.create(GOOGLE_TOKEN_URI + token))
                .retrieve()
                .toEntity(Void.class)
                .map(response -> response.getStatusCode().is2xxSuccessful());
    }
}
