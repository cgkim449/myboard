package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.vo.user.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     *
     * @return true 면 resolveArgument()가 호출됨
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasUserVoType = UserVo.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginUserAnnotation && hasUserVoType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        log.info("resolveArgument 실행");
        //TODO: 비회원, 회원
        return null;
    }
}
