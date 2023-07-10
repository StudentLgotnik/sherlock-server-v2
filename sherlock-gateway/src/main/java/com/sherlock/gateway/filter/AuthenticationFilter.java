package com.sherlock.gateway.filter;

import com.auth0.jwt.JWT;
import com.sherlock.gateway.dto.UserDetailsDto;
import com.sherlock.gateway.filter.validator.TokenValidator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String COOKIES_JWT_KEY = "jwtToken";

    private final Map<String, TokenValidator> tokenValidators;

    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(Map<String, TokenValidator> tokenValidators, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.tokenValidators = tokenValidators;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getCookies().containsKey(COOKIES_JWT_KEY)) {
                return chain.filter(exchange);
            }

            String authToken = exchange.getRequest().getCookies().get(COOKIES_JWT_KEY).get(0).getValue();

            return tokenValidators.getOrDefault(JWT.decode(authToken).getIssuer(), x -> Mono.just(false))
                    .isValid(authToken)
                    .filter(isValid -> isValid)
                    .flatMap(x -> this.toUser(authToken))
                    .map(user -> {
                        exchange.getRequest()
                                .mutate()
                                .header("x-auth-id", user.getUuid());
                        return exchange;
                    })
                    .flatMap(chain::filter);
        };
    }

    private Mono<UserDetailsDto> toUser(String token) {
        return webClientBuilder.build()
                .post()
                .uri("http://sherlock-identity/api/v1/token/to/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve().bodyToMono(UserDetailsDto.class);
    }


    public static class Config {

    }
}
