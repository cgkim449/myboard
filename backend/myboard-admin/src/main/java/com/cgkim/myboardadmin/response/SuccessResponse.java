package com.cgkim.myboardadmin.response;

import java.util.HashMap;

/**
 * 상태 코드 2xx 일때 응답 메시지
 *
 * - 목적
 *  1. 컨트롤러 메서드 리턴 타입을 ResponseEntity<SuccessResponse> 로 통일
 *  2. 응답 메시지 바디가 없는 경우 빈 객체({})로 응답
 */
//TODO: 포함관계로 바꾸기
//TODO: 빌더 패턴 적용
public class SuccessResponse extends HashMap<String, Object> {

    /**
     * SuccessResponse 에 값 저장
     *
     * @param key
     * @param value
     * @return SuccessResponse
     */
    @Override
    public SuccessResponse put(String key, Object value) {

        super.put(key, value);

        return this;
    }
}
