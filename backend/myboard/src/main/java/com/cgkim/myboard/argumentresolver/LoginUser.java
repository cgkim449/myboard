package com.cgkim.myboard.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * - 목적
 *  - 공통 코드 분리 : NativeWebRequest 에서 username 값 꺼내는 코드를 분리
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
