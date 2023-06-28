package com.sherlock.identity.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sherlock.identity.persistance.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class DefaultJwtService implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + Duration.ofDays(1).toMillis());
        return JWT.create()
                .withClaim(Claim.USER_UUID.getClaimKey(), user.getUuid().toString())
                .withClaim(Claim.USER_EMAIL.getClaimKey(), user.getEmail())
                .withClaim(Claim.USER_FIRSTNAME.getClaimKey(), user.getFirstname())
                .withClaim(Claim.USER_LASTNAME.getClaimKey(), user.getLastname())
                .withSubject(user.getNickname())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(getAlgorithm());
    }

    @Override
    public User validate(String token) {
        DecodedJWT decoded = getDecodedJwt(token);
        return User.builder()
                .uuid(UUID.fromString(decoded.getClaim(Claim.USER_UUID.getClaimKey()).asString()))
                .email(decoded.getClaim(Claim.USER_EMAIL.getClaimKey()).asString())
                .firstname(decoded.getClaim(Claim.USER_FIRSTNAME.getClaimKey()).asString())
                .lastname(decoded.getClaim(Claim.USER_LASTNAME.getClaimKey()).asString())
                .nickname(decoded.getSubject())
                .build();
    }

    @Override
    public String getClaim(String token, Claim claim) {
        return getDecodedJwt(token)
                .getClaim(claim.getClaimKey())
                .asString();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private DecodedJWT getDecodedJwt(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }
}
