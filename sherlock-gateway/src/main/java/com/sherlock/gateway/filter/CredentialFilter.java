package com.sherlock.gateway.filter;

import com.sherlock.gateway.dto.LoginDetailsDto;
import com.sherlock.gateway.dto.UserDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
@Slf4j
public class CredentialFilter extends AbstractGatewayFilterFactory<CredentialFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public CredentialFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return chain.filter(exchange);
            }

            String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Basic ")) {
                return chain.filter(exchange);
            }

            String credentials = authorizationHeader.substring("Basic ".length());
            byte[] decodedCredentials = Base64Utils.decodeFromString(credentials);
            String decodedCredentialsString = new String(decodedCredentials);

            String[] credentialsArray = decodedCredentialsString.split(":");
            String email = credentialsArray[0];
            String password = credentialsArray[1];

            return webClientBuilder.build()
                    .post()
                    .uri("http://sherlock-identity/api/v1/account/login")
                    .body(BodyInserters.fromValue(
                            LoginDetailsDto.builder()
                                    .email(email)
                                    .password(password)
                                    .build()))
                    .retrieve().bodyToMono(UserDetailsDto.class)
                    .map(user -> {
                        exchange.getRequest()
                                .mutate()
                                .header("x-auth-id", user.getUuid());
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {

    }
}
