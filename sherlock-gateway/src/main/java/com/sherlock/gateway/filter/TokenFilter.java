package com.sherlock.gateway.filter;

import com.sherlock.gateway.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public TokenFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getCookies().containsKey("jwtToken")) {
                chain.filter(exchange);
            }

            String authToken = exchange.getRequest().getCookies().get("jwtToken").get(0).getValue();

            return webClientBuilder.build()
                    .get()
                    .uri("http://sherlock-identity/api/v1/token/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                    .retrieve().bodyToMono(UserDto.class)
                    .map(userDto -> {
                        exchange.getRequest()
                                .mutate()
                                .header("x-auth-id", userDto.getUuid());
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {

    }
}
