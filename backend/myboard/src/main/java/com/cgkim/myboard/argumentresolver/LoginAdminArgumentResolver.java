package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.service.AdminService;
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
public class LoginAdminArgumentResolver implements HandlerMethodArgumentResolver {

    private final AdminService adminService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAdminAnnotation = parameter.hasParameterAnnotation(LoginAdmin.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAdminAnnotation && hasLongType;
    }

    /**
     * 컨트톨러 공통 코드 분리
     *  1. jwt 에서 username 추출
     *  2. db 에서 memberId 가져오기
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String username = (String) webRequest.getAttribute("username", SCOPE_REQUEST);
        return adminService.getAdminId(username);
    }
}
