package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.vo.user.GuestSaveRequest;
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

@Slf4j
@Component
public class GuestArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     *
     * @return true 면 resolveArgument()가 호출됨
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasGuestAnnotation = parameter.hasParameterAnnotation(Guest.class);
        boolean hasGuestSaveRequestType = GuestSaveRequest.class.isAssignableFrom(parameter.getParameterType());

        return hasGuestAnnotation && hasGuestSaveRequestType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        //TODO: webRequest.get
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String username = (String) request.getAttribute("username");

        if(username == null) { //익명 사용자이면 guestSaveRequest 유효성 검증
            GuestSaveRequest guestSaveRequest = new GuestSaveRequest();
            String name = ModelFactory.getNameForParameter(parameter); // name = "guestSaveRequest"
            WebDataBinder binder = binderFactory.createBinder(webRequest, guestSaveRequest, name);

            //TODO: 리팩토링
            String guestNickname = request.getParameter("guestNickname");
            String guestPassword = request.getParameter("guestPassword");
            String guestPasswordConfirm = request.getParameter("guestPasswordConfirm");
            guestSaveRequest = (GuestSaveRequest) binder.getTarget();
            guestSaveRequest.setGuestNickname(guestNickname);
            guestSaveRequest.setGuestPassword(guestPassword);
            guestSaveRequest.setGuestPasswordConfirm(guestPasswordConfirm);

            binder.getValidators().get(1).validate(binder.getTarget(), binder.getBindingResult());
            if (binder.getBindingResult().hasErrors()) {
                throw new BindException(binder.getBindingResult());
            }
            return guestSaveRequest;
        } else { // 로그인 사용자이면 유효성 검증 안함
            return null;
        }
    }
}
