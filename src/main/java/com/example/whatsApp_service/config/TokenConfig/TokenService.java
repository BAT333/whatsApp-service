package com.example.whatsApp_service.config.TokenConfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.whatsApp_service.Domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;



@Service
public class TokenService {
    @Value("${api.token.secret}")
    private String key;


    public String generatesToken(User principal) {
        try {
            var algorithm = Algorithm.HMAC256(key);
            return JWT.create()
                    .withIssuer("RAFAEL")
                    .withSubject(principal.getLogin())
                    .withExpiresAt(this.expire())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("ERROR IN TOKEN GENERATION", exception);
        }
    }

    private Instant expire() {
        return LocalDateTime.now().plusHours(3L).toInstant(ZoneOffset.of("-03:00"));
    }
    public String getUser(String token){
        try {
            var algorithm = Algorithm.HMAC256(key);
            return JWT.require(algorithm)
                    .withIssuer("RAFAEL")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("ERROR WHEN VALIDATING TOKEN", exception);
        }
    }
}
