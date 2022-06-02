package com.cgkim.myboard.argumentresolver;

import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckGuestPasswordArgumentResolver implements HandlerMethodArgumentResolver {

    private final BoardService boardService;

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
        Long id = extractIdFromRequestURI(requestURI); //요청 uri 에서 boardId 값 추출

        //TODO: comment 의 경우에도
        String method = request.getMethod();

        if(boardService.checkAnonymous(id)) { //익명 글인 경우에만
            String guestPassword = extractValueFromRequestBody(request); //요청 body 에서 guestPassword 값 추출
            validateGuestPassword(guestPassword); //유효성 검증
            boardService.checkGuestPassword(id, guestPassword); //비밀번호 체크
            return guestPassword;
        }

        return null;
    }

    /**
     * 요청 uri 에서 id 값 추출
     */
    private Long extractIdFromRequestURI(String requestURI) {
        StringTokenizer stringTokenizer = new StringTokenizer(requestURI, "/");
        stringTokenizer.nextToken();
        return Long.valueOf(stringTokenizer.nextToken());
    }

    /**
     * 요청 body 에서 값 추출
     */
    private String extractValueFromRequestBody(HttpServletRequest request) throws IOException {
        String key = "guestPassword";
        String value = request.getParameter(key);

        if(isRequestBodyJSON(value)) { //RequestBody 가 JSON 이면
            final int CONTENT_LENGTH = request.getContentLength();
            if(CONTENT_LENGTH > 0) {
                byte[] content = new byte[CONTENT_LENGTH];
                InputStream in = request.getInputStream();
                in.read(content, 0, CONTENT_LENGTH);
                String requestBody = new String(content, StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(requestBody);

                value =  jsonObject.getString(key);
            }
        }
        return value;
    }

    /**
     * RequestBody 가 JSON 인지 확인
     */
    private boolean isRequestBodyJSON(String guestPassword) {
        return guestPassword == null;
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
