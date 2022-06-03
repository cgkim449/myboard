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
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckGuestPasswordArgumentResolver implements HandlerMethodArgumentResolver {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ParameterExtractor parameterExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCheckGuestPasswordAnnotation = parameter.hasParameterAnnotation(CheckGuestPassword.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());
        return hasCheckGuestPasswordAnnotation && hasStringType;
    }

    /**
     * 익명 글, 댓글일때만
     *  1. 비밀번호 유효성 검증
     *  2. 비밀번호 체크(DB 조회)
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws IOException, NoSuchAlgorithmException {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Map<String, String> parameterMap = parameterExtractor.extractParameterMapFrom(request, List.of("guestPassword"));
        String guestPassword = parameterMap.get("guestPassword");

        String requestURI = request.getRequestURI();
        Long id = parameterExtractor.extractIdFrom(requestURI);
        String collection = parameterExtractor.extractCollectionFrom(requestURI);

        //익명 글, 댓글일때만 유효성 검증 & 비밀번호 체크
        if(collection.equals("boards") && boardService.isAnonymous(id)) { //익명 글이면
            validateGuestPassword(guestPassword); //유효성 검증
            boardService.checkGuestPassword(id, guestPassword); //비밀번호 체크
        } else if (collection.equals("comments") && commentService.isAnonymous(id)) { //익명 댓글이면
            validateGuestPassword(guestPassword); //유효성 검증
            commentService.checkGuestPassword(id, guestPassword); //비밀번호 체크
        }
        return guestPassword;
    }

    /**
     * 비밀번호 유효성 검증
     */
    private void validateGuestPassword(String guestPassword) {
        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }
}
