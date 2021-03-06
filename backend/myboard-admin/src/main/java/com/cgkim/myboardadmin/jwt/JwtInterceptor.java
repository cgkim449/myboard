package com.cgkim.myboardadmin.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cgkim.myboardadmin.exception.LoginRequiredException;
import com.cgkim.myboardadmin.exception.NoAuthorizationException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * - 요청 메시지 헤더에서 토큰 추출 및 검증
 * - 토큰에서 추출한 username 값을 HttpServletRequest 에 보관
 *  - 목적: 토큰 추출/검증을 컨트롤러에서 또 하지 않기 위해
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final HttpMethod ALLOW_HTTP_METHOD = HttpMethod.OPTIONS;

    private final JwtProvider jwtProvider;

    /**
     *  - 토큰 검증
     *  - 토큰에서 추출한 username 값을 HttpServletRequest 에 보관
     *      - 목적: preHandle 에서 했던 jwt 검증을 컨트롤러에서 또 하지 않기 위해
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler
    ) {

        if (ALLOW_HTTP_METHOD.matches(request.getMethod())) {
            return true;
        }

        String token = extractTokenFrom(request);
        DecodedJWT jwt = jwtProvider.validate(token);

        String username = jwt.getClaim("username").asString();
        Boolean isAdmin = jwt.getClaim("isAdmin").asBoolean();

        if(username == null || isAdmin == null) {
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
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }

        return headerValue.replace(SCHEMA + " ", "");
    }
}
