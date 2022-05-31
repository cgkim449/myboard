package com.cgkim.myboard.config.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.InvalidJwtTokenException;
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
     *
     * @return
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String requestURI = request.getRequestURI();
        log.info("JwtInterceptor 실행 {}", requestURI);

        //헤더에서 토큰 추출
        String token = extractToken(request);
        //토큰 검증
        DecodedJWT jwt = jwtProvider.validateToken(token);
        //username 추출
        String username = jwt.getSubject();
        log.info("username: {}", username);
        //request 에 보관
        request.setAttribute("username", username);
        return true;
    }

    private static final String HEADER = "Authorization";
    private static final String SCHEMA = "Bearer";

    /**
     * 헤더에서 토큰 추출
     *
     * @param request
     * @return
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);

        if(header == null) { //인증실패
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        } else {
            return header.replace(SCHEMA + " ", "");
        }
    }
}
