package com.cgkim.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: domain, web 패키지로 분리. vo, dto
//TODO: 썸네일 이미지 원본 보여주기.

//TODO: 걍 쿠키로 내려주기 그럼 프론트에서 쿠키에 저장할 필요가 없다. 그러나 쿠키 차단시의 방법을 찾아야.
// - 사용자가 쿠키 차단시 현재 로그인 방법 못씀. 다른 방법 찾아야.
@SpringBootApplication
public class MyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyboardApplication.class, args);
	}

}
