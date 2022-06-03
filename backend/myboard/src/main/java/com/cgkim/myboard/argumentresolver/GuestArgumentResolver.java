package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.validation.GuestSaveRequestValidator;
import com.cgkim.myboard.vo.user.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class GuestArgumentResolver implements HandlerMethodArgumentResolver {

    private final ParameterExtractor parameterExtractor;

    /**
     * @return true 면 resolveArgument() 동작
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasGuestAnnotation = parameter.hasParameterAnnotation(Guest.class);
        boolean hasGuestSaveRequestType = GuestSaveRequest.class.isAssignableFrom(parameter.getParameterType());
        return hasGuestAnnotation && hasGuestSaveRequestType;
    }

    /**
     * Validator 가 비로그인시에만 동작해야돼서 만듬
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        boolean isLogin = (Boolean) request.getAttribute("isLogin");
        if(isLogin) { //로그인일시 유효성 검증 안함
            return null;
        } else { //비로그인일시 유효성 검증
           GuestSaveRequest guestSaveRequest = bindObjectFrom(request); //바인딩
            validate(guestSaveRequest, webRequest, parameter, binderFactory); //유효성 검증
            return guestSaveRequest;
        }
    }

    /**
     * 바인딩
     */
    private GuestSaveRequest bindObjectFrom(HttpServletRequest request) throws IOException {
        Map<String, String> parameterMap = parameterExtractor.extractParameterMapFrom(request, List.of("guestNickname", "guestPassword", "guestPasswordConfirm"));
        String guestNickname = parameterMap.get("guestNickname");
        String guestPassword = parameterMap.get("guestPassword");
        String guestPasswordConfirm = parameterMap.get("guestPasswordConfirm");

        String collection = parameterExtractor.extractCollectionFrom(request.getRequestURI()); //요청 uri 에서 collection 추출 (boards 나 comments)
        if (collection.equals("comments")) {
            guestPasswordConfirm = guestPassword;
        }
        return GuestSaveRequest.builder()
                .guestNickname(guestNickname)
                .guestPassword(guestPassword)
                .guestPasswordConfirm(guestPasswordConfirm)
                .build();
    }

    /**
     * 유효성 검증
     */
    private void validate(
            GuestSaveRequest guestSaveRequest,
            NativeWebRequest webRequest,
            MethodParameter parameter,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        WebDataBinder binder = binderFactory.createBinder(webRequest, guestSaveRequest, ModelFactory.getNameForParameter(parameter));
        GuestSaveRequestValidator validator = (GuestSaveRequestValidator) binder.getValidators().get(1);
        validator.validate(binder.getTarget(), binder.getBindingResult());
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }

    private boolean isGuest(String username) {
        return username == null;
    }
}
