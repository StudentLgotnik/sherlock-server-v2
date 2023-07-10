package com.sherlock.gateway.filter.validator;

import reactor.core.publisher.Mono;

public interface TokenValidator {

    Mono<Boolean> isValid(String token);
}
