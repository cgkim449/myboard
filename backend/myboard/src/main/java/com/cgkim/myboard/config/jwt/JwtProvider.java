package com.cgkim.myboard.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class JwtProvider {

    private final String secretKey;
    private final long maxAge;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.token-validity}") long maxAge
    ) {
        this.secretKey = secretKey;
        this.maxAge = maxAge;
    }

    /**
     * 토큰 생성
     *
     * @param subject username
     * @return 생성한 jwt
     */
    public String createToken(String subject) {
        Date now = new Date();
        Date expires = new Date(now.getTime() + maxAge);

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expires)
                .sign(algorithm);
    }

    /**
     * 토큰 검증
     *
     * @param token 헤더에서 추출한 토큰
     * @return DecodedJWT
     */
    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }
}
