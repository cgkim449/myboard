package com.cgkim.myboard.config.jwt;

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
     *
     * @return
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        //CORS 설정
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String requestURI = request.getRequestURI();
        log.info("JwtInterceptor 실행 {}", requestURI);

        //헤더에서 토큰 추출
        String token = extractToken(request);
        log.info("token: {}", token);

        if(token != null) { //로그인 사용자이면 토큰 검증함
            //토큰 검증
            DecodedJWT jwt = jwtProvider.validateToken(token);
            //토큰에서 username 추출
            String username = jwt.getSubject();
            //username 을 request 에 보관: LoginArgumentResolver 에서 꺼내기 위해
            request.setAttribute("username", username);
        }
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
        String headerValue = request.getHeader(HEADER);

        if(headerValue == null) { //TODO: token null
            return null;
        } else {
            return headerValue.replace(SCHEMA + " ", "");
        }
    }
}
