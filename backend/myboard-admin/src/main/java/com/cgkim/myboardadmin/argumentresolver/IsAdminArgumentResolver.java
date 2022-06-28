package com.cgkim.myboardadmin.argumentresolver;

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
 * - 역할
 *  - preHandle 에서 request 객체에 보관했던 isAdmin 값을 꺼내서 컨트롤러 메서드 매개변수에 넣어주는 역할
 * - 목적
 *  - 공통 코드 분리 : NativeWebRequest 에서 isAdmin 값 꺼내는 코드를 분리
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class IsAdminArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * - 파라미터에 @LoginUser 가 붙어있는지 검사
     * - true 를 리턴하면 resolveArgument 가 실행됨
     *
     * @param parameter
     * @return boolean
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasIsAdminAnnotation = parameter.hasParameterAnnotation(IsAdmin.class);
        boolean hasBooleanType = boolean.class.isAssignableFrom(parameter.getParameterType());

        return hasIsAdminAnnotation && hasBooleanType;
    }

    /**
     * request 객체에서 isAdmin 값을 꺼내서 컨트롤러 메서드 매개변수에 넣어줌
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

        Boolean isAdmin = (Boolean) webRequest.getAttribute("isAdmin", SCOPE_REQUEST);

        return isAdmin != null && isAdmin;
    }
}