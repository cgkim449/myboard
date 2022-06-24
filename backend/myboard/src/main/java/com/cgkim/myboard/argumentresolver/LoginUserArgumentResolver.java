package com.cgkim.myboard.argumentresolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && hasStringType;
    }

    /**
     * 공통 코드 분리
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return webRequest.getAttribute("username", SCOPE_REQUEST);
    }
}