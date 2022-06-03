package com.cgkim.myboard.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        //TODO: CORS 설정
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String token = extractTokenFrom(request);
        String username = null;
        if(token != null) {
            DecodedJWT jwt = jwtProvider.validate(token);
            username = jwt.getSubject();
            request.setAttribute("username", username);
        }
        request.setAttribute("isLogin", username != null);
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
