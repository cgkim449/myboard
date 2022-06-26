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

/**
 * - 역할: preHandle 에서 request 객체에 보관했던 username 값을 꺼내서 컨트롤러 메서드 매개변수에 넣어주는 역할
 * - 목적
 *  - 공통 코드 분리
 *  - preHandle 에서 했던 jwt 검증을 컨트롤러에서 또 하지 않기 위한 목적
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * TODO: 주석
     * 
     * @param parameter
     * @return boolean
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginUserAnnotation && hasStringType;
    }

    /**
     * request 객체에서 username 값을 꺼내서 컨트롤러 메서드 매개변수에 넣어줌
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return Object
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) {

        return webRequest.getAttribute("username", SCOPE_REQUEST);
    }
}