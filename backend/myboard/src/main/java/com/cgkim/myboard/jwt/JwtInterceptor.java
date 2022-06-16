package com.cgkim.myboard.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    /**
     * 인증이 필요한 요청에 대해 토큰 검증
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = extractTokenFrom(request);
        if(token != null) {
            DecodedJWT jwt = jwtProvider.validate(token);

            String username = jwt.getClaim("username").asString();
            boolean isAdmin = jwt.getClaim("isAdmin").asBoolean();
            request.setAttribute("username", username);
            request.setAttribute("isAdmin", isAdmin);
        }
        return true;
    }

    private static final String HEADER = "Authorization";
    private static final String SCHEMA = "Bearer";

    /**
     * 헤더에서 토큰 추출
     */
    private String extractTokenFrom(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER);
        if(headerValue == null) {
            return null;
        }
        return headerValue.replace(SCHEMA + " ", "");
    }
}
