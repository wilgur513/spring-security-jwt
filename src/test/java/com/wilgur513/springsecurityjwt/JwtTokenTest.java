package com.wilgur513.springsecurityjwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class JwtTokenTest {
    @Test
    public void JWT_토큰_생성() throws InterruptedException {
        String token = JWT.create()
                .withSubject("kim")
                .withClaim("exp", Instant.now().getEpochSecond() + 1)
                .sign(Algorithm.HMAC512("secret"));

        DecodedJWT decode = JWT.require(Algorithm.HMAC512("secret")).build().verify(token);

        System.out.println(decode.getHeaderClaim("typ"));
        System.out.println(decode.getHeaderClaim("alg"));
        System.out.println(decode.getClaim("sub"));
        System.out.println(decode.getClaim("exp"));

        Thread.sleep(2000);

        assertThrows(TokenExpiredException.class, () -> {
            JWT.require(Algorithm.HMAC512("secret")).build().verify(token);
        });
    }
}
