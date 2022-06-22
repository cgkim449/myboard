package com.cgkim.myboardadmin.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cgkim.myboardadmin.exception.LoginRequiredException;
import com.cgkim.myboardadmin.exception.NoAuthorizationException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
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
        String token = extractTokenFrom(request);

        if(token == null) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }

        DecodedJWT jwt = jwtProvider.validate(token);

        String username = jwt.getClaim("username").asString();
        Boolean isAdmin = jwt.getClaim("isAdmin").asBoolean();

        if(username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        if(isAdmin == null) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }

        request.setAttribute("username", username);
        request.setAttribute("isAdmin", true);
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
