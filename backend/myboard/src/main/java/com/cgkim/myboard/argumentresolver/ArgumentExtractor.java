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
public class ArgumentExtractor {

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
    public Map<String, String> extractArgumentsFrom(HttpServletRequest request, List<String> keyList) throws IOException {
        Map<String, String> argumentMap = new HashMap<>();

        String value = request.getParameter(keyList.get(0));
        if(isRequestBodyJSON(value)) { //RequestBody 가 JSON 이면
            final int CONTENT_LENGTH = request.getContentLength();
            if(CONTENT_LENGTH > 0) {
                JSONObject jsonObject = getJsonObject(request, CONTENT_LENGTH);

                for (String key : keyList) {
                    argumentMap.put(key, jsonObject.getString(key));
                }
            }
        } else {//JSON 아니면
            for (String key : keyList) {
                argumentMap.put(key, request.getParameter(key));
            }
        }
        return argumentMap;
    }

    private JSONObject getJsonObject(HttpServletRequest request, int CONTENT_LENGTH) throws IOException {
        byte[] content = new byte[CONTENT_LENGTH];

        InputStream in = request.getInputStream();
        in.read(content, 0, CONTENT_LENGTH);
        //TODO: https://meetup.toast.com/posts/44
        in.close();

        String requestBody = new String(content, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(requestBody);
        return jsonObject;
    }

    /**
     * RequestBody 가 JSON 인지 확인
     */
    private boolean isRequestBodyJSON(String value) {
        return value == null;
    }
}
