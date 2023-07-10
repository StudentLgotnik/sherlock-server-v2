package com.sherlock.identity.security.jwt;

import com.sherlock.identity.persistance.entity.User;

public interface JwtService {
    String generateToken(User user);

    boolean isValid(String token);

    User toUser(String token);

    String getClaim(String token, Claim claim);
}
