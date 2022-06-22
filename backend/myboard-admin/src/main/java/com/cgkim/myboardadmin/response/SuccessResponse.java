package com.cgkim.myboardadmin.response;

import java.util.HashMap;

/**
 * 응답 메시지
 */

//TODO: 상속말고 포함관계로 바꾸기
public class SuccessResponse extends HashMap<String, Object> {

    @Override
    public SuccessResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
