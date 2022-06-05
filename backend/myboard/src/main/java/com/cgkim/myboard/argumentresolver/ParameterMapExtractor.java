package com.cgkim.myboard.argumentresolver;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Component
public class ParameterMapExtractor {

    private static StringTokenizer stringTokenizer;

    /**
     * 요청 uri 에서 id 값 추출
     */
    public Long extractIdFrom(String requestURI) {
        stringTokenizer = new StringTokenizer(requestURI, "/");
        stringTokenizer.nextToken();
        return Long.valueOf(stringTokenizer.nextToken());
    }

    /**
     * 요청 uri 에서 collection 추출
     */
    public String extractCollectionFrom(String requestURI) {
        stringTokenizer = new StringTokenizer(requestURI, "/");
        return stringTokenizer.nextToken();
    }

    /**
     * 요청 body 에서 값 추출
     */
    public Map<String, String> extractParameterMapFrom(HttpServletRequest request, List<String> parameterNameList) throws IOException {
        Map<String, String> parameterMap = new HashMap<>();

        final int CONTENT_LENGTH = request.getContentLength();
        if(isRequestBodyJSON(request) && CONTENT_LENGTH > 0) { //RequestBody 가 JSON 이면
            JSONObject jsonObject = getRequestBodyFrom(request, CONTENT_LENGTH); //RequestBody 얻기
            getParameterMapFrom(jsonObject, parameterNameList, parameterMap); //ParameterMap 얻기
        } else {//JSON 아니면
            getParameterMapFrom(request, parameterNameList, parameterMap); //ParameterMap 얻기
        }
        return parameterMap;
    }

    private void getParameterMapFrom(HttpServletRequest request, List<String> parameterNameList, Map<String, String> parameterMap) {
        for (String name : parameterNameList) {
            parameterMap.put(name, request.getParameter(name));
        }
    }

    private void getParameterMapFrom(JSONObject jsonObject, List<String> parameterNameList, Map<String, String> parameterMap) {
        for (String name : parameterNameList) {
            parameterMap.put(name, jsonObject.getString(name));
        }
    }

    //TODO: https://meetup.toast.com/posts/44 공부
    private JSONObject getRequestBodyFrom(HttpServletRequest request, int CONTENT_LENGTH) throws IOException {
        byte[] content = new byte[CONTENT_LENGTH];

        InputStream in = request.getInputStream();
        in.read(content, 0, CONTENT_LENGTH);
        in.close();

        String requestBody = new String(content, StandardCharsets.UTF_8);
        return new JSONObject(requestBody);
    }

    /**
     * RequestBody 가 JSON 인지 확인
     */
    private boolean isRequestBodyJSON(HttpServletRequest request) {
        return request.getParameterMap().isEmpty();
    }
}
