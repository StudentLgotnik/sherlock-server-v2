package com.sherlock.identity.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Claim {
    USER_EMAIL("email"),
    NICKNAME("name"),
    USER_FIRSTNAME("given_name"),
    USER_LASTNAME("family_name");

    private final String claimKey;
}
