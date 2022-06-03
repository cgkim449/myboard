package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && hasLongType;
    }

    /**
     * 목적: 컨트톨러 공통 코드 분리
     *  1. jwt 에서 username 추출
     *  2. db 에서 userId 가져오기
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String username = (String) webRequest.getAttribute("username", SCOPE_REQUEST);
        return userService.getUserId(username);
    }
}
