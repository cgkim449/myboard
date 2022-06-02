package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckGuestPasswordArgumentResolver implements HandlerMethodArgumentResolver {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ArgumentExtractor argumentExtractor;

    /**
     * @return true 면 resolveArgument()가 호출됨
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasCheckGuestPasswordAnnotation = parameter.hasParameterAnnotation(CheckGuestPassword.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());

        return hasCheckGuestPasswordAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws IOException {
        //TODO: HttpServletRequest 안쓰기
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String requestURI = request.getRequestURI(); //요청 uri
        Long id = argumentExtractor.extractIdFrom(requestURI); //요청 uri 에서 id 값 추출
        String collection = argumentExtractor.extractCollectionFrom(requestURI);

        String guestPassword = null;
        //TODO: 코드 중복
        if(collection.equals("boards") && boardService.checkAnonymous(id)) { //익명 글인지 체크
            //요청 body 에서 guestPassword 값 추출
            Map<String, String> argumentMap = argumentExtractor.extractArgumentsFrom(request, List.of("guestPassword"));
            guestPassword = argumentMap.get("guestPassword");

            validateGuestPassword(guestPassword); //유효성 검증
            boardService.checkGuestPassword(id, guestPassword); //비밀번호 체크
        } else if (collection.equals("comments") && commentService.checkAnonymous(id)) { // 익명 댓글인지 체크
            Map<String, String> argumentMap = argumentExtractor.extractArgumentsFrom(request, List.of("guestPassword"));
            guestPassword = argumentMap.get("guestPassword");

            validateGuestPassword(guestPassword); //유효성 검증
            commentService.checkGuestPassword(id, guestPassword); //비밀번호 체크
        }

        return guestPassword;
    }

    /**
     * guestPassword 유효성 검증
     */
    private void validateGuestPassword(String guestPassword) {
        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }
}
