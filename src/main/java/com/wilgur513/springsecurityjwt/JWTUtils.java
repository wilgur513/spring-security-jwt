package com.wilgur513.springsecurityjwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;

public class JWTUtils {
    private String secret = "hello";
    private Algorithm al = Algorithm.HMAC512(secret);

    public String generate(String userId) {
        return JWT.create()
            .withSubject(userId)
            .withClaim("exp", Instant.now().getEpochSecond() + 3000)
            .sign(al);
    }

    public VerifyResult verify(String token) {
        try{
            DecodedJWT decode = JWT.require(al).build().verify(token);
            return VerifyResult.builder().userId(decode.getSubject()).result(true).build();
        }catch(JWTVerificationException e) {
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder().userId(decode.getSubject()).result(false).build();
        }
    }
}
