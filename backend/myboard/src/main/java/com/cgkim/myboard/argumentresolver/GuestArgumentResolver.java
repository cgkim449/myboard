package com.cgkim.myboard.argumentresolver;

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
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class GuestArgumentResolver implements HandlerMethodArgumentResolver {

    private final ArgumentExtractor argumentExtractor;

    /**
     * @return true 면 resolveArgument() 동작
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
        //TODO: HttpServletRequest 안쓰기
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String username = (String) request.getAttribute("username");

        //TODO: 리팩토링
        if(isGuest(username)) { //Guest 일때만 유효성 검증
            String requestURI = request.getRequestURI(); //요청 uri
            String collection = argumentExtractor.extractCollectionFrom(requestURI); //요청 uri 에서 collection 추출

            GuestSaveRequest guestSaveRequest = new GuestSaveRequest();
            String name = ModelFactory.getNameForParameter(parameter); // name = "guestSaveRequest"
            WebDataBinder binder = binderFactory.createBinder(webRequest, guestSaveRequest, name);

            String guestNickname = null;
            String guestPassword = null;
            String guestPasswordConfirm = null;

            if(collection.equals("boards")) {
                guestNickname = request.getParameter("guestNickname");
                guestPassword = request.getParameter("guestPassword");
                guestPasswordConfirm = request.getParameter("guestPasswordConfirm");
            } else if (collection.equals("comments")) {
                Map<String, String> argumentMap = argumentExtractor.extractArgumentsFrom(request, List.of("guestNickname", "guestPassword"));
                guestNickname = argumentMap.get("guestNickname");
                guestPassword = argumentMap.get("guestPassword");
                guestPasswordConfirm = guestPassword; //Validator 재사용땜에
            }

            guestSaveRequest.setGuestNickname(guestNickname);
            guestSaveRequest.setGuestPassword(guestPassword);
            guestSaveRequest.setGuestPasswordConfirm(guestPasswordConfirm);

            validateGuestSaveRequest(binder); // 유효성 검증
            return guestSaveRequest;
        } else { // 로그인 사용자이면 유효성 검증 안함
            return null;
        }
    }

    private void validateGuestSaveRequest(WebDataBinder binder) throws BindException {
        binder.getValidators().get(1).validate(binder.getTarget(), binder.getBindingResult());
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }

    private boolean isGuest(String username) {
        return username == null;
    }
}
